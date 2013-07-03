package br.usp.icmc.communication;


import org.apache.cordova.DroidGap;

import android.util.Log;
import android.webkit.WebView;
import br.usp.icmc.project.Controler;

public class Communication {
	private String TAG = "Communication";
	private WebView mAppView;
	private DroidGap mGap;
	private Controler controler;

	public Communication(DroidGap gap, WebView view){
		mAppView = view;
	    mGap = gap;
	    controler = new Controler();
	}
	
	public String insert(String codigo){
		String returnMessage = "Mensagem Default. Codigo Recebido: " + codigo;
		try{
			returnMessage =	controler.insertAction(codigo);
		}catch(Exception ex){
			Log.e(TAG,ex.toString());
			returnMessage = "Unknow error";
		}
		return returnMessage;
	}
	
	public String update(){
		String returnMessage = "{}";
		try{
			returnMessage =	controler.updateAction();
		}catch(Exception ex){
			Log.e(TAG,ex.toString());
			returnMessage = "Unknow error";
		}
		return returnMessage;
	}
}
