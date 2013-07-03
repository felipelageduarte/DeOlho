/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package br.usp.icmc.project;

import android.os.Bundle;
import br.usp.icmc.communication.Communication;
import br.usp.icmc.database.DBManager;

import org.apache.cordova.*;

public class Project extends DroidGap{
	private Communication communication;
	
    @Override
    public void onCreate(Bundle savedInstanceState){
    	
        super.onCreate(savedInstanceState);
        super.init();
        
        communication = new Communication(this, appView);
        appView.addJavascriptInterface(communication, "communication");
        
        super.loadUrl(Config.getStartUrl());
        
        DBManager.onCreate(this);

    }
}

