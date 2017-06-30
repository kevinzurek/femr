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
package femr.ui.models.research;

import femr.common.models.MissionItem;
import java.util.List;

public class FilterViewModel {

    private String startDate;
    private String endDate;
    private List<MissionItem> MissionTrips; //Andrew Trip Filter
    private Integer MissionTripId; //Andrew Trip Filter


    public List<MissionItem> getMissionTrips() { return MissionTrips; } //Andrew Trip Filter

    public void setMissionTrips(List<MissionItem> MissionTrips) { this.MissionTrips = MissionTrips; } //Andrew Trip Filter

    public Integer getMissionTripId() { return MissionTripId; }

    public void setMissionTripId(Integer MissionTripId) { this.MissionTripId = MissionTripId; }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}

