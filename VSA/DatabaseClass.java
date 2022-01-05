package com2027.cw.dp00405.vsa;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

public class DatabaseClass extends AsyncTask<String, Void, String> {

    String type;
    String[] contents;
    ArrayList<Boolean> isTrue = new ArrayList<Boolean>();
    ArrayList<String[]> bookmarks = new ArrayList<String[]>();

    ConfirmFragment confirmFragment;
    LoginFragment loginFragment;
    BookmarkFragment bookmarkFragment;

    public DatabaseClass(ConfirmFragment parent, LoginFragment parent1, BookmarkFragment parent2){
        this.confirmFragment = parent;
        this.loginFragment = parent1;
        this.bookmarkFragment = parent2;
    }

    @Override
    protected String doInBackground(String... voids) {
        String result = "";
        this.type = voids[0].split("#")[voids[0].split("#").length - 1];

        ArrayList<String> temp = new ArrayList<>(Arrays.asList(voids[0].split("#")));
        temp.remove(voids[0].split("#").length - 1);
        this.contents = temp.toArray(new String[0]);

        Log.e("log_type", type);
        //Log.e("log_string_reslt", voids[voids.length]);
        if (type.equals("RECEIVE")) {
            result = database("http://DESKTOP-T3V0VAH/receive.php", result);
            database("http://DESKTOP-T3V0VAH/receiveitem.php", null);
        } else if ((type.equals("LOGIN"))) {
            result = database("http://DESKTOP-T3V0VAH/account_login.php", result);
        } else if ((type.equals("REGISTER"))) {
            database("http://DESKTOP-T3V0VAH/account_register.php", result);
        } else if ((type.equals("DELETE"))) {
            database("http://DESKTOP-T3V0VAH/account_delete.php", result);
        } else if ((type.equals("UPDATE"))) {
            database("http://DESKTOP-T3V0VAH/account_update.php", result);
        } else if ((type.equals("BOOKSET"))) {
            database("http://DESKTOP-T3V0VAH/bookmarks_set.php", result);
        } else if ((type.equals("BOOKGET"))) {
            result = database("http://DESKTOP-T3V0VAH/bookmarks_get.php", result);
        } else if ((type.equals("BOOKREMOVE"))) {
            database("http://DESKTOP-T3V0VAH/bookmarks_remove.php", result);
        } else if ((type.equals("BOOKUPDATE"))) {
            database("http://DESKTOP-T3V0VAH/bookmarks_update.php", result);
        }
        return result;
    }

    private String database (String connection, String result) {
        try {
            URL url = new URL(connection);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            OutputStream outputStream = urlConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            if (connection.equals("http://DESKTOP-T3V0VAH/receive.php")) {
                //receive
                String data = "";
                for (String product : contents) {
                    if (data.equals("")) {
                        data += product.split(",")[0];
                    } else {
                        data += "," + product.split(",")[0];
                    }
                }
                Log.e("log_data_during", data);
                data = URLEncoder.encode("productid", "UTF-8") + "=" + URLEncoder.encode(data, "UTF-8");
                Log.e("log_data_after", data);
                bufferedWriter.write(data);
            } else if (connection.equals("http://DESKTOP-T3V0VAH/receiveitem.php")) {
                //receiveItem
                String product_data = "";
                String tag_data = "";
                for (String product : contents) {
                    if (product_data.equals("") && tag_data.equals("")) {
                        product_data += product.split(",")[0];
                        tag_data += product.split(",")[8];
                    } else {
                        product_data += "," + product.split(",")[0];
                        tag_data += "," + product.split(",")[8];
                    }
                }
                String data = URLEncoder.encode("productid", "UTF-8") + "=" + URLEncoder.encode(product_data, "UTF-8")
                        + "&" + URLEncoder.encode("tagid", "UTF-8") + "=" + URLEncoder.encode(tag_data, "UTF-8");
                bufferedWriter.write(data);
            } else if (connection.equals("http://DESKTOP-T3V0VAH/account_login.php")) {
                //login
                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(contents[0].split(",")[0], "UTF-8");
                bufferedWriter.write(data);
            } else if (connection.equals("http://DESKTOP-T3V0VAH/account_register.php")) {
                //register
                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(contents[0].split(",")[0], "UTF-8")
                        + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(contents[0].split(",")[1], "UTF-8")
                        + "&" + URLEncoder.encode("forename", "UTF-8") + "=" + URLEncoder.encode(contents[0].split(",")[2], "UTF-8")
                        + "&" + URLEncoder.encode("surname", "UTF-8") + "=" + URLEncoder.encode(contents[0].split(",")[3], "UTF-8");
                bufferedWriter.write(data);
            } else if (connection.equals("http://DESKTOP-T3V0VAH/account_delete.php")) {
                //delete
                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(contents[0].split(",")[0], "UTF-8");
                bufferedWriter.write(data);
            } else if (connection.equals("http://DESKTOP-T3V0VAH/account_update.php")) {
                //update
                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(contents[0].split(",")[0], "UTF-8")
                        + "&" + URLEncoder.encode("forename", "UTF-8") + "=" + URLEncoder.encode(contents[0].split(",")[2], "UTF-8")
                        + "&" + URLEncoder.encode("surname", "UTF-8") + "=" + URLEncoder.encode(contents[0].split(",")[3], "UTF-8")
                        + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(contents[0].split(",")[1], "UTF-8");
                bufferedWriter.write(data);
            } else if (connection.equals("http://DESKTOP-T3V0VAH/bookmarks_set.php")) {
                //setBookmark
                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(contents[0].split(",")[0], "UTF-8")
                        + "&" + URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(contents[0].split(",")[1], "UTF-8")
                        + "&" + URLEncoder.encode("description", "UTF-8") + "=" + URLEncoder.encode(contents[0].split(",")[2], "UTF-8")
                        + "&" + URLEncoder.encode("link", "UTF-8") + "=" + URLEncoder.encode(contents[0].split(",")[3], "UTF-8");
                bufferedWriter.write(data);
            } else if (connection.equals("http://DESKTOP-T3V0VAH/bookmarks_get.php")) {
                //getBookmark
                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(contents[0], "UTF-8");
                bufferedWriter.write(data);
            } else if (connection.equals("http://DESKTOP-T3V0VAH/bookmarks_remove.php")) {
                //removeBookmark
                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(contents[0].split(",")[0], "UTF-8")
                        + "&" + URLEncoder.encode("link", "UTF-8") + "=" + URLEncoder.encode(contents[0].split(",")[1], "UTF-8");
                bufferedWriter.write(data);
            } else if (connection.equals("http://DESKTOP-T3V0VAH/bookmarks_update.php")) {
                //updateBookmark
                String data = URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(contents[0].split(",")[1], "UTF-8")
                        + "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(contents[0].split(",")[0], "UTF-8")
                        + "&" + URLEncoder.encode("description", "UTF-8") + "=" + URLEncoder.encode(contents[0].split(",")[2], "UTF-8")
                        + "&" + URLEncoder.encode("link", "UTF-8") + "=" + URLEncoder.encode(contents[0].split(",")[3], "UTF-8");
                bufferedWriter.write(data);
            }

            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "ISO-8859-1"));
            if (connection.equals("http://DESKTOP-T3V0VAH/receive.php") || connection.equals("http://DESKTOP-T3V0VAH/account_login.php")
                    || connection.equals("http://DESKTOP-T3V0VAH/bookmarks_get.php")) {
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
            }
            bufferedReader.close();
            inputStream.close();
            urlConnection.disconnect();

            return result;
        }
        catch (MalformedURLException e) {
            Log.e("log_url", e.getMessage());
        }
        catch (IOException e) {
            Log.e("log_io", e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        Log.e("log_postS", s);

        if (s != null && !s.equals("")) {
            if (this.type.equals("RECEIVE")) {
                int i = 0;
                for (String product : contents) {
                    String[] data = s.split("\\},\\{");
                    data[i] = data[i].replaceAll("[^\\w\\.\\ \\:\\,]", "");
                    Log.e("log_data", data[i]);
                    Log.e("log_product", product);

                    Log.e("111", data[i].split(",")[0].split(":")[1] + "----" + (product.split(",")[1]));
                    Log.e("222", data[i].split(",")[1].split(":")[1] + "----" + (product.split(",")[2]));
                    Log.e("333", data[i].split(",")[2].split(":")[1] + "----" + (product.split(",")[3]));
                    Log.e("444", data[i].split(",")[3].split(":")[1] + "----" + (product.split(",")[4]));
                    Log.e("555", data[i].split(",")[4].split(":")[1] + "----" + (product.split(",")[5]));
                    Log.e("666", data[i].split(",")[5].split(":")[1] + "----" + (product.split(",")[6].replace("/", "")));

                    if (data[i].split(",")[0].split(":")[1].equals(product.split(",")[1]) &&
                            data[i].split(",")[1].split(":")[1].equals(product.split(",")[2]) &&
                            data[i].split(",")[2].split(":")[1].equals(product.split(",")[3]) &&
                            data[i].split(",")[3].split(":")[1].equals(product.split(",")[4]) &&
                            data[i].split(",")[4].split(":")[1].equals(product.split(",")[5]) &&
                            data[i].split(",")[5].split(":")[1].equals(product.split(",")[6].replace("/", ""))) {
                        isTrue.add(true);
                    } else {
                        isTrue.add(false);
                    }
                    i += 1;
                }
                Log.e("log_istrue", isTrue.get(0).toString());
                confirmFragment.setIsTrue(isTrue);
            } else if (this.type.equals("LOGIN")) {
                String data = s.replaceAll("[^\\w\\.\\ \\:\\,]", "");
                try {
                    if (data.split(",")[0].split(":")[1].equals(this.contents[0].split(",")[1])) {
                        loginFragment.setAccount(true, this.contents[0].split(",")[0], data.split(",")[0].split(":")[1], data.split(",")[1].split(":")[1], data.split(",")[2].split(":")[1]);
                    } else {
                        loginFragment.setAccount(false, "", "", "", "");
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

            } else if (this.type.equals("BOOKGET")) {
                String data = s.replaceAll("[^\\w\\.\\,\\:\\-\\/\\\\]", "");
                Log.e("log_bookget", data);
                try {
                    int j = 0;
                    String[] e = new String[3];
                    for (int i = 0; i < data.split(",").length; i++) {
                        e[j] = data.split(",")[i].split(":")[1];
                        if (e[j].equals("https") || e[j].equals("http")) {
                            e[j] = "https:" + data.split(",")[i].split(":")[2];
                        }
                        if (j >= 2) {
                            j = 0;
                            Log.e("log_booadadadkget", e[0] +  " " + e[1] + " " + e[2]);
                            bookmarks.add(e);
                            e = new String[3];
                        } else {
                            j += 1;
                        }
                    }
                    bookmarkFragment.setBookmarks(bookmarks);
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
