package net.motimaa.motiyhteisot.storage.sql;

import com.zaxxer.hikari.HikariDataSource;
import net.motimaa.motiyhteisot.MotiYhteisot;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQL extends SQLType {

    private final MotiYhteisot motiYhteisot = MotiYhteisot.getInstance();

    private final HikariDataSource hikari;

    public MySQL() {

        hikari = new HikariDataSource();

        hikari.setJdbcUrl("jdbc:mysql://" + motiYhteisot.getConfig().getString("host") + ":" + motiYhteisot.getConfig().getInt("port") + "/" + motiYhteisot.getConfig().getString("database") + "?useSSL=false&allowPublicKeyRetrieval=true");

        hikari.setUsername(motiYhteisot.getConfig().getString("username"));
        hikari.setPassword(motiYhteisot.getConfig().getString("password"));

        super.SAVE_ORGANIZATION = "INSERT INTO organizations (id, name, abbreviation, created, balance, base, wins) VALUES ((?), (?), (?), (?), (?), (?), (?)) ON DUPLICATE KEY UPDATE name=(?), abbreviation=(?), balance=(?), base=(?), wins=(?)";
        super.SAVE_MEMBER = "INSERT INTO members (uuid, name, organization, role, joined) VALUES ((?), (?), (?), (?), (?)) ON DUPLICATE KEY UPDATE name=(?), role=(?)";
        super.SAVE_ROLE = "INSERT INTO roles (id, name, organization, weight, permissions) VALUES ((?), (?), (?), (?), (?)) ON DUPLICATE KEY UPDATE name=(?), weight=(?), permissions=(?)";
        super.SAVE_APPLICATION = "INSERT INTO applications (id, uuid, name, organization, description) VALUES ((?), (?), (?), (?), (?)) ON DUPLICATE KEY UPDATE name=(?)";

    }

    @Override
    protected Connection getConnection() throws SQLException {
        return hikari.getConnection();
    }
}
