package net.motimaa.motiyhteisot.storage.sql;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class SQLType {

    final String TABLE_ORGANIZATION = "CREATE TABLE IF NOT EXISTS `organizations` (`id` VARCHAR(36), `name` TEXT, `abbreviation` TEXT, `created` LONG, `balance` DOUBLE, `base` TEXT, `wins` INT, PRIMARY KEY (id))";
    final String TABLE_MEMBER = "CREATE TABLE IF NOT EXISTS `members` (`uuid` VARCHAR(36), `name` VARCHAR(16), `organization` VARCHAR(36), `role` VARCHAR(36), `joined` LONG, PRIMARY KEY (uuid))";
    final String TABLE_ROLE = "CREATE TABLE IF NOT EXISTS `roles` (`id` VARCHAR(36), `name` TEXT, `organization` VARCHAR(36), `weight` TINYINT(127), `permissions` TEXT, PRIMARY KEY (id))";
    final String TABLE_APPLICATION = "CREATE TABLE IF NOT EXISTS `applications` (`id` VARCHAR(36), `uuid` VARCHAR(36), `name` VARCHAR(16), `organization` VARCHAR(36), `description` TEXT, PRIMARY KEY (id))";

    String SAVE_ORGANIZATION;
    String SAVE_MEMBER;
    String SAVE_ROLE;
    String SAVE_APPLICATION;

    final String DELETE_ORGANIZATION = "DELETE FROM organizations WHERE id=(?)";
    final String DELETE_MEMBER = "DELETE FROM members WHERE uuid=(?)";
    final String DELETE_ROLE = "DELETE FROM roles WHERE id=(?)";
    final String DELETE_APPLICATION = "DELETE FROM applications WHERE id=(?)";


    final String SELECT_ORGANIZATION = "SELECT id, name, abbreviation, created, balance, base, wins FROM organizations";
    final String SELECT_MEMBER = "SELECT uuid, name, organization, role, joined FROM members";
    final String SELECT_ROLE = "SELECT id, name, organization, weight, permissions FROM roles";
    final String SELECT_APPLICATION = "SELECT id, uuid, name, organization, description FROM applications";


    final String SELECT_CORRUPT_PLAYERS = "SELECT uuid FROM members WHERE NOT EXISTS(SELECT id FROM roles WHERE id=role)";
    final String SELECT_CORRUPT_MEMBERS = "SELECT uuid FROM members WHERE NOT EXISTS(SELECT organization FROM organizations WHERE organization=id)";
    final String SELECT_CORRUPT_ROLES = "SELECT id FROM roles WHERE NOT EXISTS(SELECT id FROM organizations WHERE organization=id)";
    final String SELECT_CORRUPT_APPLICATIONS = "SELECT id FROM applications WHERE NOT EXISTS(SELECT id FROM organizations WHERE organization=id)";
    final String SELECT_CORRUPT_ORGANIZATIONS = "SELECT id FROM organizations WHERE NOT EXISTS(SELECT id FROM members WHERE organization=id)";

    protected abstract Connection getConnection() throws SQLException;


}
