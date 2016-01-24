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
package femr.ui.models.triage;

import femr.common.models.PatientItem;
import femr.common.models.SettingItem;

import java.util.Map;

public class IndexViewModel {

    //Patient form data
    private PatientItem patient;
    private String ageClassification;
    private Integer bloodPressureSystolic;
    private Integer bloodPressureDiastolic;
    private Integer heartRate;
    private Float temperature;
    private Integer respiratoryRate;
    private Float oxygenSaturation;
    private Integer heightFeet;
    private Integer heightInches;
    private Float weight;
    private Integer glucose;
    //Encounter form data
    private String chiefComplaint;
        //holds multiple chief complaints if they exist.
        // TODO: use this for single chief complaints?
    private String chiefComplaintsJSON;
    private Integer weeksPregnant;

    private String patientPhotoCropped;



    //flag to determine if a user would like to delete the patient's image
    //TODO: make not public
    public boolean deletePhoto;



    //system setting values
    private SettingItem settings;
    //all possible options for age classification
    private Map<String, String> possibleAgeClassifications;
    //this gets set to true when a patient has an open encounter so that a link to medical can be displayed
    private boolean linkToMedical = false;


    public PatientItem getPatient() {
        return patient;
    }

    public void setPatient(PatientItem patient) {
        this.patient = patient;
    }

    public String getAgeClassification() {
        return ageClassification;
    }

    public void setAgeClassification(String ageClassification) {
        this.ageClassification = ageClassification;
    }

    public Integer getBloodPressureSystolic() {
        return bloodPressureSystolic;
    }

    public void setBloodPressureSystolic(Integer bloodPressureSystolic) {
        this.bloodPressureSystolic = bloodPressureSystolic;
    }

    public Integer getBloodPressureDiastolic() {
        return bloodPressureDiastolic;
    }

    public void setBloodPressureDiastolic(Integer bloodPressureDiastolic) {
        this.bloodPressureDiastolic = bloodPressureDiastolic;
    }

    public Integer getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Integer heartRate) {
        this.heartRate = heartRate;
    }

    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public Integer getRespiratoryRate() {
        return respiratoryRate;
    }

    public void setRespiratoryRate(Integer respiratoryRate) {
        this.respiratoryRate = respiratoryRate;
    }

    public Float getOxygenSaturation() {
        return oxygenSaturation;
    }

    public void setOxygenSaturation(Float oxygenSaturation) {
        this.oxygenSaturation = oxygenSaturation;
    }

    public Integer getHeightFeet() {
        return heightFeet;
    }

    public void setHeightFeet(Integer heightFeet) {
        this.heightFeet = heightFeet;
    }

    public Integer getHeightInches() {
        return heightInches;
    }

    public void setHeightInches(Integer heightInches) {
        this.heightInches = heightInches;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Integer getGlucose() {
        return glucose;
    }

    public void setGlucose(Integer glucose) {
        this.glucose = glucose;
    }

    public String getChiefComplaint() {
        return chiefComplaint;
    }

    public void setChiefComplaint(String chiefComplaint) {
        this.chiefComplaint = chiefComplaint;
    }

    public String getChiefComplaintsJSON() {
        return chiefComplaintsJSON;
    }

    public void setChiefComplaintsJSON(String chiefComplaintsJSON) {
        this.chiefComplaintsJSON = chiefComplaintsJSON;
    }

    public Integer getWeeksPregnant() {
        return weeksPregnant;
    }

    public void setWeeksPregnant(Integer weeksPregnant) {
        this.weeksPregnant = weeksPregnant;
    }

    public String getPatientPhotoCropped() {
        return patientPhotoCropped;
    }

    public void setPatientPhotoCropped(String patientPhotoCropped) {
        this.patientPhotoCropped = patientPhotoCropped;
    }

    public boolean isDeletePhoto() {
        return deletePhoto;
    }

    public SettingItem getSettings() {
        return settings;
    }

    public void setSettings(SettingItem settings) {
        this.settings = settings;
    }

    public Map<String, String> getPossibleAgeClassifications() {
        return possibleAgeClassifications;
    }

    public void setPossibleAgeClassifications(Map<String, String> possibleAgeClassifications) {
        this.possibleAgeClassifications = possibleAgeClassifications;
    }

    public boolean isLinkToMedical() {
        return linkToMedical;
    }

    public void setLinkToMedical(boolean linkToMedical) {
        this.linkToMedical = linkToMedical;
    }
}
