package AsyncTasks;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.fuemi.eisbaer.Activitys.MainActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;

import Interfaces.AsyncResponse;
import Items.Order;


public class GetNewsFromInternetTask extends AsyncTask<String,Integer,String> {

    ArrayList<Order> orderOfMerrit = new ArrayList<>();
    Context context;

    private AsyncResponse asyncResponse;

    public GetNewsFromInternetTask(Context context, AsyncResponse asyncResponse){
        this.context = context.getApplicationContext();
        this.asyncResponse = asyncResponse;
    }

    /*
        läuft im Hintergrund ab, startet durch .execute aufruf in der Activity
     */

    @Override
    protected String doInBackground(String... strings) {

        //getInternetConnection
        if(checkingForInternetAccess()){
            getOrderOfMeritFromInternet();
        }else{
            //fehlermeldung,kein internetzugriff
            System.out.println("Kein Internetzugriff");
            publishProgress(5, 0);
        }


        return null;
    }

    /*
        wird durch pubishProgress aufgerufen
        übergibt Fortschritt über das Interface AsyncResponse an die Activity
     */

    @Override
    protected void onProgressUpdate(Integer... values) {
        asyncResponse.onUpdateProgress(values[0], values[1]);

    }

    /*
        sendet einen Ping aufruf an eine netzadresse
        überprüft so, ob zugriff aufs internet hergestellt werden kann
        ist ausm internet xD
     */

    // TCP/HTTP/DNS (depending on the port, 53=DNS, 80=HTTP, etc.)
    private boolean checkingForInternetAccess() {
            publishProgress(5,1);
            try {
                int timeoutMs = 1500;
                Socket sock = new Socket();
                SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

                sock.connect(sockaddr, timeoutMs);
                sock.close();

                return true;
            } catch (IOException e) { return false; }

    }


    //greift auf die Dart1.de webseite zu
    //liest die HTML aus
    //bestimmt die Daten der Order of Merrit
    //Gibt diese als ArrayList<Order> zurück
    private void getOrderOfMeritFromInternet() {
        publishProgress(10,1);
        try {
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost("https://www.darts1.de/ranglisten/PDC-Order-of-Merit.php");

            HttpResponse httpResponse = httpClient.execute(httpPost);

            HttpEntity httpEntity = httpResponse.getEntity();

            InputStream input = httpEntity.getContent();

            StringBuffer buffer = new StringBuffer();
            String newLine;

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input, "ISO-8859-1"));

            boolean getData = false;
            while((newLine = bufferedReader.readLine()) != null){

                //Tabelle Startet
                if(newLine.contains("Stand")){
                    getData = true;
                }
                if(newLine.contains("ranglisten")){
                    getData = false;
                }
                if(getData & newLine.contains("<tr><td>")){
                    //split data
                    String[] seperated = newLine.split("</td><td>");
                    //entfernen der tabellenzeichen
                    //entfernen der leerzeichen
                    for(int i = 0; i < seperated.length;i++){
                        seperated[i] = seperated[i].replaceAll("<tr>","");
                        seperated[i] = seperated[i].replaceAll("<td>","");
                        seperated[i] = seperated[i].replaceAll("</tr>","");
                        seperated[i] = seperated[i].replaceAll("</td>","");
                        // wenn Namensstring noch <img... enthält, wird der String bei < getrennt & alles vor < behalten
                        if(seperated[i].contains("<")){
                            String[] cutEnd = seperated[i].split("<");
                            seperated[i] = cutEnd[0];
                        }
                        seperated[i] = seperated[i].trim();
                    }
                    //entfernen der leerzeichen

                    orderOfMerrit.add(new Order(seperated));
                    buffer.append(seperated[0]);
                    buffer.append(seperated[1]);
                    buffer.append(seperated[2]);
                    buffer.append("\n");
                }

            }

            input.close();

            System.out.print(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            publishProgress(10,0);
        }
    }


    /*
        wird ausgeführt, nachdem doInBackground abgeschlossen ist
        startet neue Activity, wenn die ArrayList orderOfMerrit mindestens 1 Element enthält
        finish funktioniert noch nicht!
     */
    @Override
    protected void onPostExecute(String result) {
        // execution of result of Long time consuming operation
        if(orderOfMerrit.size() >= 1){
            Intent i = new Intent(context, MainActivity.class);

            Bundle b = new Bundle();
            b.putSerializable("OrderOfMerrit",orderOfMerrit);
            i.putExtra("OrderOfMerrit", b);
            context.startActivity(i);
        }

        //((Activity) context).finish();
    }


    @Override
    protected void onPreExecute() {

    }



}
