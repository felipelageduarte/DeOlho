package br.usp.icmc.project;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.LinkedHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.database.Cursor;
import android.util.Log;
import br.usp.icmc.database.Encomenda;
import br.usp.icmc.database.Situacao;
import br.usp.icmc.database.Update;

public class Controler {

	private static String TAG = "Controler";	

	public String insertAction(String codigo) {
		Log.v(TAG, "Controler insert()");

		Encomenda encomenda;

		try {
			encomenda = new Encomenda(codigo);
			encomenda.insert();
		} catch (android.database.SQLException sqlex) {
			Log.e(TAG, "SQL Error: " + sqlex.toString());
			return "SQL Error: " + sqlex.toString();
		} catch (Exception ex) {
			Log.e(TAG,
					"Element not inserted in database. Unknow Error: "
							+ ex.toString());
			return "Unknow Error: " + ex.toString();
		}

		try {
			Update.run(encomenda);
		} catch (Exception ex) {
			Log.e(TAG, "Unknow Error: " + ex.toString());
			return "Problem on update status. Unknow Error: " + ex.toString();
		}

		return "Codigo " + codigo + " inserido com sucesso!";
	}
	
	public String updateAction() {
		Log.v(TAG, "Controler update()");	
		
		String resultXML = "";
		
		try{
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		
		// root elements
		Document doc = documentBuilder.newDocument();
		Element rootElement = doc.createElement("data");
		doc.appendChild(rootElement);

		Cursor encomendaCursor = Encomenda.findAll();
		Log.v(TAG, "Numero de encomendas retornadas: " + encomendaCursor.getCount());
		
		Encomenda encomenda;
		int encomendaCount = 0;
		while ((encomenda = Encomenda.next(encomendaCursor)) != null) {
			Log.d(TAG, "Encomenda recuperada codigo: " + encomenda.getCodigo() 
							+ " - hash: " + encomenda.getHash());
			
			try {
				Update.run(encomenda);
			} catch (Exception ex) {
				Log.e(TAG, "Unknow Error: " + ex.toString());
				return "Problem on update status. Unknow Error: " + ex.toString();
			}
			
			Cursor situacaoCursor = Situacao.findByCodigo(encomenda.getCodigo());
			Log.v(TAG, "Numero de situacoes retornadas para encomenda  "
							+ encomenda.getCodigo() + " = "
							+ situacaoCursor.getCount());
			
			// staff elements
			Element enc = doc.createElement("encomenda");
			enc.setAttribute("codigo", encomenda.getCodigo());
			rootElement.appendChild(enc);

			Situacao situacao;
			int situacaoCount = 0;
			while ((situacao = Situacao.next(situacaoCursor)) != null) {
				Log.d(TAG, "codigo: " + encomenda.getCodigo() + " / data: "
								+ situacao.getData() + " / local: "
								+ situacao.getLocal() + " / situacao: "
								+ situacao.getSituacao());
				
				Element sit = doc.createElement("situacao");				
				
				Element data = doc.createElement("data");
				data.appendChild(doc.createTextNode(situacao.getData()));
				sit.appendChild(data);
				
				Element local = doc.createElement("local");
				local.appendChild(doc.createTextNode(situacao.getLocal()));
				sit.appendChild(local);
				
				Element status = doc.createElement("status");
				status.appendChild(doc.createTextNode(situacao.getSituacao()));
				sit.appendChild(status);
			
				enc.appendChild(sit);
			}
		}
		
		TransformerFactory transfac = TransformerFactory.newInstance();
		Transformer trans = transfac.newTransformer();
		StringWriter sw = new StringWriter();
		StreamResult result = new StreamResult(sw);
		DOMSource source = new DOMSource(doc);
		trans.transform(source, result);
		resultXML = sw.toString();
		
		}catch(Exception ex){
			
		}
		
		return resultXML;
	}

}
