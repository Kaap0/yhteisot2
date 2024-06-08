package net.motimaa.motiyhteisot.storage.sql;

import net.motimaa.motiyhteisot.MotiYhteisot;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite extends SQLType {

    private final MotiYhteisot motiYhteisot = MotiYhteisot.getInstance();

    private final String url;

    public SQLite() {

        url = "jdbc:sqlite:" + motiYhteisot.getDataFolder() + File.separator + "motiyhteisot.db";

        super.SAVE_ORGANIZATION = "INSERT INTO organizations (id, name, abbreviation, created, balance, base, wins) VALUES ((?), (?), (?), (?), (?), (?), (?)) ON CONFLICT(id) DO UPDATE SET name=(?), abbreviation=(?), balance=(?), base=(?), wins=(?)";
        super.SAVE_MEMBER = "INSERT INTO members (uuid, name, organization, role, joined) VALUES ((?), (?), (?), (?), (?)) ON CONFLICT(uuid) DO UPDATE SET name=(?), role=(?)";
        super.SAVE_ROLE = "INSERT INTO roles (id, name, organization, weight, permissions) VALUES ((?), (?), (?), (?), (?)) ON CONFLICT(id) DO UPDATE SET name=(?), weight=(?), permissions=(?)";
        super.SAVE_APPLICATION = "INSERT INTO applications (id, uuid, name, organization, description) VALUES ((?), (?), (?), (?), (?)) ON CONFLICT(id) DO UPDATE SET name=(?)";

    }

    @Override
    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }
}
