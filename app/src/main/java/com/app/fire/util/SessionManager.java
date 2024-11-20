package com.app.fire.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    static final String KEY_LOGIN =
            "login",
            KEY_ID = "id",
            KEY_NAME = "name",
            KEY_TYPE_USER = "type_user",
            KEY_JURUSAN = "jurusan",
            KEY_INSTITUTE = "institute",
            KEY_EMAIL = "email",
            KEY_HP = "nomor_hp",
            KEY_NUMBER = "number",
            KEY_AVATAR = "avatar",
            KEY_SKILL = "skill";

    public static SharedPreferences getSharedPreference(Context context) {
        return context.getSharedPreferences(
                "zona", Context.MODE_PRIVATE);
    }

    public static void setCustData(Context context, String name, String custId, String email, String noHp,String avatar,  int typeUser) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putBoolean(KEY_LOGIN, true);
        editor.putString(KEY_ID, custId);
        editor.putInt(KEY_TYPE_USER, typeUser);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_AVATAR, avatar);
        editor.putString(KEY_HP, noHp);
        editor.apply();
    }
    public static void setAvatar(Context context, String avatar) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_AVATAR, avatar);
        editor.apply();
    }
    public static void setCustData(Context context, String name, String custId, String email, String skill, String noHp,String avatar, int typeUser) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putBoolean(KEY_LOGIN, true);
        editor.putString(KEY_ID, custId);
        editor.putInt(KEY_TYPE_USER, typeUser);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_SKILL, skill);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_HP, noHp);
        editor.putString(KEY_AVATAR, avatar);
        editor.apply();
    }


    public static String getId(Context context) {
        return getSharedPreference(context).getString(KEY_ID, "");
    }

    public static int getTypeUser(Context context) {
        return getSharedPreference(context).getInt(KEY_TYPE_USER, 2);
    }


    public static String getName(Context context) {
        return getSharedPreference(context).getString(KEY_NAME, "");
    }
    public static String getEmail(Context context) {
        return getSharedPreference(context).getString(KEY_EMAIL, "");
    }
    public static String getJurusan(Context context) {
        return getSharedPreference(context).getString(KEY_JURUSAN, "");
    }
    public static String getSkill(Context context) {
        return getSharedPreference(context).getString(KEY_SKILL, "");
    }
    public static String getAvatar(Context context) {
        return getSharedPreference(context).getString(KEY_AVATAR, "");
    }
    public static String getNoHp(Context context) {
        return getSharedPreference(context).getString(KEY_HP, "");
    }

    public static String getNumber(Context context) {
        return getSharedPreference(context).getString(KEY_NUMBER, "");
    }

    public static Boolean getIsLogin(Context context) {
        return getSharedPreference(context).getBoolean(KEY_LOGIN, false);
    }

    public static void clearData(Context context) {
        getSharedPreference(context).edit().clear().apply();
    }

}
