/*
     fEMR - fast Electronic Medical Records
     Copyright (C) 2014  Team fEMR

     fEMR is free software: you can redistribute it and/or modify
     it under the terms of the GNU General Public License as published by
     the Free Software Foundation, either version 3 of the License, or
     (at your option) any later version.

     fEMR is distributed in the hope that it will be useful,
     but WITHOUT ANY WARRANTY; without even the implied warranty of
     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
     GNU General Public License for more details.

     You should have received a copy of the GNU General Public License
     along with fEMR.  If not, see <http://www.gnu.org/licenses/>. If
     you have any questions, contact <info@teamfemr.org>.
*/
package femr.business.services.system;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Query;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import femr.business.helpers.LogicDoer;
import femr.business.helpers.QueryProvider;
import femr.business.services.core.IResearchService;
import femr.common.dtos.ServiceResponse;
import femr.common.models.ResearchExportItem;
import femr.data.daos.IRepository;
import femr.data.models.core.*;
import femr.data.models.core.research.IResearchEncounter;
import femr.data.models.mysql.PatientEncounterTabField;
import femr.data.models.mysql.Vital;
import femr.data.models.mysql.research.ResearchEncounter;
import femr.data.models.mysql.research.ResearchEncounterVital;
import femr.util.calculations.dateUtils;
import femr.util.stringhelpers.CSVWriterGson;
import femr.util.stringhelpers.GsonFlattener;
import femr.util.stringhelpers.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ResearchService implements IResearchService {

    private final IRepository<IResearchEncounter> researchEncounterRepository;
    private final IRepository<IVital> vitalRepository;
    private final IRepository<IPatientEncounterTabField> patientEncounterTabFieldRepository;


    /**
     * Initializes the research service and injects the dependence
     */
    @Inject
    public ResearchService(IRepository<IResearchEncounter> researchEncounterRepository,
                           IRepository<IVital> vitalRepository,
                           IRepository<IPatientEncounterTabField> patientEncounterTabFieldRepository) {

        this.researchEncounterRepository = researchEncounterRepository;
        this.vitalRepository = vitalRepository;
        this.patientEncounterTabFieldRepository = patientEncounterTabFieldRepository;
    }


    private ResearchExportItem createResearchExportItem(IResearchEncounter encounter, UUID patientId){

        //this item is used to populate one line in the CSV file.
        ResearchExportItem exportitem = new ResearchExportItem();

        IPatient patient = encounter.getPatient();

        // Patient Id
        exportitem.setPatientId(patientId);

        // Age
        Integer age = (int)Math.floor(dateUtils.getAgeAsOfDateFloat(patient.getAge(), encounter.getDateOfTriageVisit()));
        exportitem.setAge(age);

        // Gender
        String gender = StringUtils.outputStringOrNA(patient.getSex());
        exportitem.setGender(gender);

        // Pregnancy Status
        Integer wksPregnant = getWeeksPregnant(encounter);
        exportitem.setWeeksPregnant(wksPregnant);

        // Week Pregnant
        if( wksPregnant > 0 ){
            exportitem.setIsPregnant(true);
        }
        else{
            exportitem.setIsPregnant(false);
        }

        // Chief Complaints
        List<String> chiefComplaints = new ArrayList<>();
        for (IChiefComplaint c : encounter.getChiefComplaints()) {

            chiefComplaints.add(c.getValue());
        }
        exportitem.setChiefComplaints(chiefComplaints);

        // Prescriptions - Prescribed and Dispensed
        List<String> prescribed = new ArrayList<>();
        List<String> dispensed = new ArrayList<>();
        if( encounter.getPatientPrescriptions() != null ) {
            for (IPatientPrescription p : encounter.getPatientPrescriptions()) {

                if( p.getDateDispensed() != null ){

                    dispensed.add(p.getMedication().getName());
                }

                prescribed.add(p.getMedication().getName());
            }
        }
        exportitem.setDispensedMedications(dispensed);
        exportitem.setPrescribedMedications(prescribed);

        // Tab Fields
        ExpressionList<PatientEncounterTabField> patientEncounterTabFieldExpressionList = QueryProvider.getPatientEncounterTabFieldQuery()
                .where()
                .eq("patient_encounter_id", encounter.getId());
        List<? extends IPatientEncounterTabField> existingPatientEncounterTabFields = patientEncounterTabFieldRepository.find(patientEncounterTabFieldExpressionList);
        Map<String, String> tabFields = new HashMap<>();
        if( existingPatientEncounterTabFields.size() > 0 ){

            for( IPatientEncounterTabField tf : existingPatientEncounterTabFields ){

                tabFields.put(tf.getTabField().getName(), tf.getTabFieldValue());
            }
        }
        exportitem.setTabFieldMap(tabFields);

        // Vitals
        Map<Integer, ResearchEncounterVital> vitalMap = encounter.getEncounterVitals();
        Map<String, Float> vitals = new HashMap<>();
        for( ResearchEncounterVital vital : vitalMap.values() ){

            vitals.put(vital.getVital().getName(), vital.getVitalValue());
        }
        exportitem.setVitalMap(vitals);

        //month and year of the encounter
        exportitem.setDayOfVisit(dateUtils.getFriendlyDateMonthYear(encounter.getDateOfTriageVisit()));

        return exportitem;

    }

    @Override
    public ServiceResponse<File> exportPatientsByTrip(Integer tripId){

        ServiceResponse<File> response = new ServiceResponse<>();

        // Build Query based on Filters
        Query<ResearchEncounter> researchEncounterQuery = QueryProvider.getResearchEncounterQuery();

        researchEncounterQuery
                .fetch("patient")
                .fetch("patientPrescriptions")
                .fetch("patientPrescriptions.medication");

        ExpressionList<ResearchEncounter> researchEncounterExpressionList = researchEncounterQuery.where();

        // -1 is default from form
        if ( tripId != null && tripId != -1 ) {

            researchEncounterExpressionList.eq("missionTrip.id",tripId);
        }

        researchEncounterExpressionList.isNull("patient.isDeleted");
        researchEncounterExpressionList.orderBy().desc("date_of_triage_visit");
        researchEncounterExpressionList.findList();

        List<? extends IResearchEncounter> patientEncounters = researchEncounterRepository.find(researchEncounterExpressionList);


        // As new patients are encountered, generate a UUID to represent them in the export file
        Map<Integer, UUID> patientIdMap = new HashMap<>();

        // Format patient data for the csv file
        List<ResearchExportItem> researchExportItemsForCSVExport = new ArrayList<>();

        for(IResearchEncounter patientEncounter : patientEncounters ){

            UUID patient_uuid;

            // If UUID already generated for patient, use that
            if( patientIdMap.containsKey(patientEncounter.getPatient().getId()) ){

                patient_uuid = patientIdMap.get(patientEncounter.getPatient().getId());
            }
            // otherwise generate and store for potential additional patient encounters
            else{

                patient_uuid = UUID.randomUUID();
                patientIdMap.put(patientEncounter.getPatient().getId(), patient_uuid);
            }

            ResearchExportItem item = createResearchExportItem(patientEncounter, patient_uuid);
            researchExportItemsForCSVExport.add(item);
        }

        File eFile = createCsvFile(researchExportItemsForCSVExport);

        response.setResponseObject(eFile);

        return response;
    }

    /**
     * Creates a csv file from a list of ResearchExportItems
     *
     * @param encounters the encounters to be in the file
     * @return a csv formatted file of the encounters
     */
    private File createCsvFile( List<ResearchExportItem> encounters ){

        // Make File and get path
        String csvFilePath = LogicDoer.getCsvFilePath();
        //Ensure folder exists, if not, create it
        File f = new File(csvFilePath);
        if (!f.exists())
            f.mkdirs();

        // trailing slash is included in path
        //CurrentUser currentUser = sessionService.retrieveCurrentUserSession();
        SimpleDateFormat format = new SimpleDateFormat("MMddyy-HHmmss");
        String timestamp = format.format(new Date());
        String csvFileName = csvFilePath+"export-"+timestamp+".csv";
        File eFile = new File(csvFileName);
        boolean fileCreated = false;
        if(!eFile.exists()) {
            try {
                fileCreated = eFile.createNewFile();
            }
            catch( IOException e ){

                e.printStackTrace();
            }
        }

        if( fileCreated ) {

            Gson gson = new Gson();
            JsonParser gsonParser = new JsonParser();
            String jsonString = gson.toJson(encounters);

            GsonFlattener parser = new GsonFlattener();
            CSVWriterGson writer = new CSVWriterGson();

            try {

                List<Map<String, String>> flatJson = parser.parse(gsonParser.parse(jsonString).getAsJsonArray());
                writer.writeAsCSV(flatJson, csvFileName);

            } catch (FileNotFoundException e) {

                e.printStackTrace();
            }
        }

        return eFile;
    }


    private Integer getWeeksPregnant( IResearchEncounter encounter ){

        ExpressionList<Vital> wkPregnantQuery = QueryProvider.getVitalQuery().where().eq("name", "weeksPregnant");
        IVital vital = vitalRepository.findOne(wkPregnantQuery);

        ResearchEncounterVital wksPregnantVital = encounter.getEncounterVitals().get(vital.getId());

        if ( wksPregnantVital != null && wksPregnantVital.getVitalValue() > 0) {
            return Math.round(wksPregnantVital.getVitalValue() );
        }
        else{
            return 0;
        }
    }
}