package br.usp.icmc.database;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.util.Log;
import br.icmc.usp.exception.ControlerException;
import br.usp.icmc.util.HashCode;

public class Update {
	
	private static String TAG = "Update";
	private static String urlCorreios = "http://websro.correios.com.br/sro_bin/txect01$.QueryList?P_LINGUA=001&P_TIPO=001&P_COD_UNI=";

	public static void run(Encomenda encomenda) throws ControlerException {
		Document doc;
		Elements tableData;

		// Conecta no sistema dos correios, dump da página
		try {
			doc = Jsoup.connect(urlCorreios + encomenda.getCodigo()).get();
		} catch (Exception ex) {
			Log.e(TAG, "Erro ao atualizar códigos. " + ex.toString());
			throw new ControlerException(
					"Não foi possível conectar no servidor dos correios.");
		}

		// Verifica se houve atualização ou não da pagina dos correios
		// atraves do calculo do Hash do conteudo da Página
		String hash = HashCode.sha1(doc.text());
		if (hash != encomenda.getHash()) {
			Log.v(TAG, "Atualizando informacoes da encomenda codigo: "
					+ encomenda.getCodigo());

			// parse do HTML da tabela
			tableData = doc.select("td");
			String data = "", local = "", situacao = "", rowspan = "";
			int numElemInseridos = 0;
			int tableSize = tableData.size();

			for (int i = 3; i < tableSize;) {
				try {
					rowspan = tableData.get(i).attr("rowspan");
					data = tableData.get(i++).text();
					local = tableData.get(i++).text();
					situacao = tableData.get(i++).text();

					// WorkAround necessario para verificar se o proximo
					// elemento é uma data ou um local
					if (Integer.valueOf(rowspan) == 2) {
						local += " - " + tableData.get(i++).text();
					}

					try {
						Situacao s = new Situacao(encomenda, data, local,
								situacao);
						if(s.insert() == -1){
							Log.e(TAG, "Element not inserted in database. Unknow Error");
						}else{
							numElemInseridos++;
						}
					} catch (android.database.SQLException sqlex) {
						Log.e(TAG, "SQL Error: " + sqlex.toString());
					} catch (Exception ex) {
						Log.e(TAG,
								"Element not inserted in database. Unknow Error: "
										+ ex.toString());
					}

				} catch (Exception ex) {
					Log.e(TAG, "rowSpan: " + rowspan + " / data: " + data
							+ " / local: " + local + " / situacao: " + situacao);
					Log.e(TAG, "Unknow Error: tableSize: " + tableSize
							+ " / Message:" + ex.toString());
					break;
				}
			}

			Log.v(TAG,"Numero de situações inseridas: " + numElemInseridos);
			
			// atualiza dados da encomenda no banco de dados
			encomenda.setHash(hash);
			Log.d(TAG, "Hash Calculado. Hash: " + hash);
			encomenda.update();

		} else {
			Log.v(TAG,
					"Nao houve atualizacao da pagina. Codigo: "
							+ encomenda.getCodigo());
		}

	}
}
