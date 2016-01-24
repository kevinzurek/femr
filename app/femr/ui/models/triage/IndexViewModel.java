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

    //search info
    private boolean searchError = false;
    //flag to determine if a user would like to delete the patient's image file
    boolean deletePhoto;
    //system setting values
    private SettingItem settings;
    //all possible options for age classification
    private Map<String, String> possibleAgeClassifications;
    //hidden link TODO: wtf??
    private boolean linkToMedical = false;



}
