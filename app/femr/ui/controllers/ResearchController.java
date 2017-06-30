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
package femr.ui.controllers;

import com.google.gson.Gson;
import com.google.inject.Inject;
import femr.business.services.core.IMissionTripService;
import femr.common.dtos.ServiceResponse;
import femr.common.models.*;
import femr.data.models.mysql.*;
import femr.ui.models.research.json.ResearchGraphDataModel;
import femr.common.dtos.CurrentUser;
import femr.business.services.core.IResearchService;
import femr.business.services.core.ISessionService;
import femr.ui.helpers.security.AllowedRoles;
import femr.ui.helpers.security.FEMRAuthenticated;
import femr.ui.models.research.json.ResearchItemModel;
import femr.ui.views.html.research.index;
import femr.ui.models.research.FilterViewModel;
import femr.util.stringhelpers.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Security.Authenticated(FEMRAuthenticated.class)
@AllowedRoles({Roles.RESEARCHER})
public class ResearchController extends Controller {

    private final FormFactory formFactory;
    private IResearchService researchService;
    private ISessionService sessionService;
    private IMissionTripService missionTripService; //Andrew Trip Filter

    /**
     * Research Controller constructor that Injects the services indicated by the parameters
     *
     * @param sessionService    {@link ISessionService}
     * @param researchService   {@link IResearchService}
     */
    @Inject
    public ResearchController(FormFactory formFactory, ISessionService sessionService, IResearchService researchService, IMissionTripService missionTripService) {

        this.formFactory = formFactory;
        this.researchService = researchService;
        this.sessionService = sessionService;
        this.missionTripService = missionTripService; //Andrew Trip Filter
    }

    public Result indexGet() {

        FilterViewModel filterViewModel = new FilterViewModel();

        //Grabbing mission city ID's Andrew Trip Filter
        ServiceResponse<List<MissionItem>> missionItemServiceResponse = missionTripService.retrieveAllTripInformation();
        if (missionItemServiceResponse.hasErrors())
            throw new RuntimeException();
        filterViewModel.setMissionTrips(missionItemServiceResponse.getResponseObject());

        // Set Default Start (30 Days Ago) and End Date (Today)
        Calendar today = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        filterViewModel.setEndDate(dateFormat.format(today.getTime()));
        today.add(Calendar.DAY_OF_MONTH, -120);
        filterViewModel.setStartDate(dateFormat.format(today.getTime()));


        CurrentUser currentUserSession = sessionService.retrieveCurrentUserSession();

        return ok(index.render(currentUserSession, filterViewModel));
    }


    /**
     * Called when a user wants to export the data to a CSV file.
     */
    public Result exportPost(int tripId) {

        final Form<FilterViewModel> filterViewModelForm = formFactory.form(FilterViewModel.class);

        ServiceResponse<File> exportServiceResponse = researchService.exportPatientsByTrip(tripId);

        File csvFile = exportServiceResponse.getResponseObject();

        response().setHeader("Content-disposition", "attachment; filename=" + csvFile.getName());

        return ok(csvFile).as("application/x-download");
    }

}
