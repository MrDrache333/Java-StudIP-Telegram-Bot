/*
 *   $class.name
 *   MovieBlogLookup
 *
 *   Created by Keno Oelrichs Garcia on $today.date
 *   Copyright (c) 2018 Keno Oelrichs Garcia. All rights reserved.
 */

package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

import static utils.Debugger.printDebug;
import static utils.Debugger.writeerror;

/**
 * Project: Stud.IP-App
 * Package: StudIP_App.utils
 * Created by Keno Oelrichs García on 14.11.16.
 */
public class Settings {

    private static ArrayList<String[]> liste = new ArrayList<>();

    private Properties props;   //Eigenschaften
    private File Savefile;  //Datei zum speichern und laden
    private boolean Protect;

    /**
     * Instantiates a new Settings.
     *
     * @param Saveto  the saveto
     * @param protect the protect
     */
//Constructor
    public Settings(File Saveto, boolean protect) {
        this.props = new Properties();
        this.Savefile = Saveto;
        this.Protect = protect;
    }

    /**
     * Reset properties.
     */
//Einstellungen zuruecksetzen
    public void resetProperties() {
        this.props.clear();
    }

    /**
     * Gets property.
     *
     * @param key the key
     * @return the property
     */
//Einstellungsparameter zurueckgeben
    public String getProperty(String key) {
        String out = null;
        try {
            if (Protect) {
                out = Password.entlock(props.getProperty(Password.lock(key)));
            } else {
                out = props.getProperty(key);
            }
        } catch (Exception e) {
            //Sout("Wert: " + key + " wurde nicht in " + Savefile.getName() + " gefunden!");
        }
        if (out == null || out.equals("null")) return "";
        else
            return out;
    }

    /**
     * Sets property.
     *
     * @param key   the key
     * @param value the value
     */
//Einstellungs Parameter setzen
    public void setProperty(String key, String value) {
        if (value.equals("")) value = "null";
        try {
            if (Protect) {
                props.setProperty(Password.lock(key), Password.lock(value));
            } else {
                props.setProperty(key, value);
            }
        } catch (Exception e) {
            writeerror(e);
        }
    }

    /**
     * Add property.
     *
     * @param key   the key
     * @param value the value
     */
//Einstellungs Parameter hinzufuegen
    public void addProperty(String key, String value) {
        if (value.equals("")) value = "null";
        try {
            if (Protect) {
                props.put(Password.lock(key), Password.lock(value));
            } else {
                props.put(key, value);
            }
        } catch (Exception e) {
            writeerror(e);
        }
    }

    /**
     * Load properties boolean.
     *
     * @return the boolean
     */
//Versuchen, die Einstellungen zu laden und je nach erfolg boolean zurueckgeben
    public Boolean loadProperties() {
        Boolean loaded;
        try {
            if (this.Savefile.exists()) {   //Wenn die Datei existiert -> Mithilfe von Properties Einstellungen laden
                InputStream fis = new FileInputStream(this.Savefile);
                this.props.loadFromXML(fis);
                fis.close();
                loaded = true;
            } else
                loaded = false;
        } catch (Exception e) {
            loaded = false;
        }
        //Debugging
        if (!loaded) printDebug("ERROR", "Faild to Load Settings in " + this.Savefile.getAbsolutePath());
        return loaded;
    }

    /**
     * Save properties boolean.
     *
     * @return the boolean
     */
//Einstellungen speichern
    public boolean saveProperties() {
        try {   //Versuchen, einstellungen zu speichern und dementsprechend boolean zurueckgeben
            OutputStream fos = new FileOutputStream(this.Savefile);
            this.props.storeToXML(fos, "");
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Gets file.
     *
     * @return the file
     */
//Einstellungen speicherDatei zurueckgeben
    public File getFile() {
        return this.Savefile;
    }

//STATIC FUNKTIONS

    /**
     * Tostring string.
     *
     * @return the string
     */
//Debugfunktion
    public String tostring() {
        String string = "";
        try {
            FileReader fr = new FileReader(this.Savefile);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                string += line + "\n";
            }
        } catch (Exception e) {
            string = "null";
        }
        return string;
    }
}