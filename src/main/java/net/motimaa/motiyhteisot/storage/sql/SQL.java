package net.motimaa.motiyhteisot.storage.sql;

import net.motimaa.motiyhteisot.Manager;
import net.motimaa.motiyhteisot.MotiYhteisot;
import net.motimaa.motiyhteisot.Utils;
import net.motimaa.motiyhteisot.organization.Application;
import net.motimaa.motiyhteisot.organization.Member;
import net.motimaa.motiyhteisot.organization.Organization;
import net.motimaa.motiyhteisot.organization.Role;
import net.motimaa.motiyhteisot.storage.Saveable;
import net.motimaa.motiyhteisot.storage.Storage;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQL implements Storage {

    private final MotiYhteisot motiYhteisot = MotiYhteisot.getInstance();

    private final SQLType sqlType;

    private final Object databaseLock = new Object();

    public SQL(SQLType sqlType) {
        this.sqlType = sqlType;
        createTables();
    }

    private void createTables() {

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = sqlType.getConnection();
            preparedStatement = connection.prepareStatement(sqlType.TABLE_ORGANIZATION);
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement(sqlType.TABLE_MEMBER);
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement(sqlType.TABLE_ROLE);
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement(sqlType.TABLE_APPLICATION);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Utils.severe("Ei saavutettu tietokantaa, sammutetaan plugin");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(motiYhteisot);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void save(Saveable saveable) {

        new BukkitRunnable() {

            public void run() {

                synchronized (databaseLock) {
                    Connection connection = null;
                    PreparedStatement preparedStatement = null;

                    try {
                        connection = sqlType.getConnection();

                        if (saveable instanceof Organization organization) {

                            preparedStatement = connection.prepareStatement(sqlType.SAVE_ORGANIZATION);
                            preparedStatement.setString(1, organization.getOrganizationID().toString());
                            preparedStatement.setString(2, organization.getName());
                            preparedStatement.setString(3, organization.getAbbreviation());
                            preparedStatement.setLong(4, organization.getCreated());
                            preparedStatement.setDouble(5, organization.getBalance());
                            if (organization.getBase() == null) {
                                preparedStatement.setNull(6, 0);
                            } else {
                                preparedStatement.setString(6, Utils.locationToString(organization.getBase()));
                            }
                            preparedStatement.setInt(7, organization.getWins());
                            preparedStatement.setString(8, organization.getName());
                            preparedStatement.setString(9, organization.getAbbreviation());
                            preparedStatement.setDouble(10, organization.getBalance());

                            if (organization.getBase() == null) {
                                preparedStatement.setNull(11, 0);
                            } else {
                                preparedStatement.setString(11, Utils.locationToString(organization.getBase()));
                            }
                            preparedStatement.setInt(12, organization.getWins());

                        } else if (saveable instanceof Member member) {

                            preparedStatement = connection.prepareStatement(sqlType.SAVE_MEMBER);
                            preparedStatement.setString(1, member.getUuid().toString());
                            preparedStatement.setString(2, member.getName());
                            preparedStatement.setString(3, member.getOrganization().getOrganizationID().toString());
                            preparedStatement.setString(4, member.getRole().getRoleID().toString());
                            preparedStatement.setLong(5, member.getJoined());

                            preparedStatement.setString(6, member.getName());
                            preparedStatement.setString(7, member.getRole().getRoleID().toString());

                        } else if (saveable instanceof Role role) {

                            preparedStatement = connection.prepareStatement(sqlType.SAVE_ROLE);
                            preparedStatement.setString(1, role.getRoleID().toString());
                            preparedStatement.setString(2, role.getName());
                            preparedStatement.setString(3, role.getOrganization().getOrganizationID().toString());
                            preparedStatement.setShort(4, role.getWeight());
                            preparedStatement.setString(5, Utils.listToString(role.getPermissions()));

                            preparedStatement.setString(6, role.getName());
                            preparedStatement.setShort(7, role.getWeight());
                            preparedStatement.setString(8, Utils.listToString(role.getPermissions()));

                        } else if (saveable instanceof Application application) {

                            preparedStatement = connection.prepareStatement(sqlType.SAVE_APPLICATION);
                            preparedStatement.setString(1, application.getApplicationID().toString());
                            preparedStatement.setString(2, application.getUuid().toString());
                            preparedStatement.setString(3, application.getName());
                            preparedStatement.setString(4, application.getOrganization().getOrganizationID().toString());
                            preparedStatement.setString(5, application.getDescription());

                            preparedStatement.setString(6, application.getName());

                        } else {
                            throw new RuntimeException();
                        }

                        preparedStatement.executeUpdate();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        if (connection != null) {
                            try {
                                connection.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        if (preparedStatement != null) {
                            try {
                                connection.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            }

        }.runTaskAsynchronously(motiYhteisot);
    }

    @Override
    public void delete(Saveable saveable) {

        new BukkitRunnable() {

            public void run() {

                synchronized (databaseLock) {
                    Connection connection = null;
                    PreparedStatement preparedStatement = null;

                    try {
                        connection = sqlType.getConnection();

                        if (saveable instanceof Organization organization) {

                            preparedStatement = connection.prepareStatement(sqlType.DELETE_ORGANIZATION);
                            preparedStatement.setString(1, organization.getOrganizationID().toString());

                        } else if (saveable instanceof Member member) {

                            preparedStatement = connection.prepareStatement(sqlType.DELETE_MEMBER);
                            preparedStatement.setString(1, member.getUuid().toString());

                        } else if (saveable instanceof Role role) {

                            preparedStatement = connection.prepareStatement(sqlType.DELETE_ROLE);
                            preparedStatement.setString(1, role.getRoleID().toString());

                        } else if (saveable instanceof Application application) {

                            preparedStatement = connection.prepareStatement(sqlType.DELETE_APPLICATION);
                            preparedStatement.setString(1, application.getApplicationID().toString());
                        } else {
                            throw new RuntimeException();
                        }

                        preparedStatement.executeUpdate();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        if (connection != null) {
                            try {
                                connection.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        if (preparedStatement != null) {
                            try {
                                connection.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

            }
        }.runTaskAsynchronously(motiYhteisot);
    }

    @Override
    public void load(boolean debug) {

        new BukkitRunnable() {
            @Override
            public void run() {

                synchronized (databaseLock) {
                    long start = System.currentTimeMillis();
                    Utils.info("Aloitetaan yhteisojen, roolien, hakemuksien ja jasenien lataus...");

                    Connection connection = null;
                    PreparedStatement preparedStatement = null;

                    try {

                        connection = sqlType.getConnection();

                        preparedStatement = connection.prepareStatement(sqlType.SELECT_CORRUPT_PLAYERS);
                        ResultSet resultSet = preparedStatement.executeQuery();
                        while (resultSet.next()) {
                            preparedStatement = connection.prepareStatement(sqlType.DELETE_MEMBER);
                            preparedStatement.setString(1, resultSet.getString("uuid"));
                            preparedStatement.executeUpdate();
                            Utils.info("Poistettu korruptoitunut pelaaja " + resultSet.getString("uuid"));
                        }

                        preparedStatement = connection.prepareStatement(sqlType.SELECT_CORRUPT_ORGANIZATIONS);
                        ResultSet resultSet4 = preparedStatement.executeQuery();
                        while (resultSet4.next()) {
                            preparedStatement = connection.prepareStatement(sqlType.DELETE_ORGANIZATION);
                            preparedStatement.setString(1, resultSet4.getString("id"));
                            preparedStatement.executeUpdate();
                            Utils.info("Poistettu korruptoitunut yhteiso " + resultSet4.getString("id"));
                        }

                        preparedStatement = connection.prepareStatement(sqlType.SELECT_CORRUPT_APPLICATIONS);
                        ResultSet resultSet3 = preparedStatement.executeQuery();
                        while (resultSet3.next()) {
                            preparedStatement = connection.prepareStatement(sqlType.DELETE_APPLICATION);
                            preparedStatement.setString(1, resultSet3.getString("id"));
                            preparedStatement.executeUpdate();
                            Utils.info("Poistettu korruptoitunut hakemus " + resultSet3.getString("id"));
                        }

                        preparedStatement = connection.prepareStatement(sqlType.SELECT_CORRUPT_ROLES);
                        ResultSet resultSet2 = preparedStatement.executeQuery();
                        while (resultSet2.next()) {
                            preparedStatement = connection.prepareStatement(sqlType.DELETE_ROLE);
                            preparedStatement.setString(1, resultSet2.getString("id"));
                            preparedStatement.executeUpdate();
                            Utils.info("Poistettu korruptoitunut rooli " + resultSet2.getString("id"));
                        }

                        preparedStatement = connection.prepareStatement(sqlType.SELECT_CORRUPT_MEMBERS);
                        ResultSet resultSet1 = preparedStatement.executeQuery();
                        while (resultSet1.next()) {
                            preparedStatement = connection.prepareStatement(sqlType.DELETE_MEMBER);
                            preparedStatement.setString(1, resultSet1.getString("uuid"));
                            preparedStatement.executeUpdate();
                            Utils.info("Poistettu korruptoitunut jasen " + resultSet1.getString("uuid"));
                        }


                        preparedStatement = connection.prepareStatement(sqlType.SELECT_ORGANIZATION);
                        ResultSet resultSet5 = preparedStatement.executeQuery();
                        while (resultSet5.next()) {
                            if (debug) {
                                Utils.info("Ladataan yhteisoa " + UUID.fromString(resultSet5.getString("id")));
                            }
                            new Organization(UUID.fromString(resultSet5.getString("id")), resultSet5.getString("name"), resultSet5.getString("abbreviation"), resultSet5.getLong("created"), resultSet5.getDouble("balance"), resultSet5.getString("base"), resultSet5.getInt("wins"));
                        }

                        preparedStatement = connection.prepareStatement(sqlType.SELECT_ROLE);
                        ResultSet resultSet6 = preparedStatement.executeQuery();
                        while (resultSet6.next()) {
                            if (debug) {
                                Utils.info("Ladataan roolia " + UUID.fromString(resultSet6.getString("id")));
                            }
                            new Role(UUID.fromString(resultSet6.getString("id")), resultSet6.getString("name"), motiYhteisot.getManager().getOrganization(UUID.fromString(resultSet6.getString("organization"))), resultSet6.getByte("weight"), resultSet6.getString("permissions"));
                        }

                        preparedStatement = connection.prepareStatement(sqlType.SELECT_MEMBER);
                        ResultSet resultSet7 = preparedStatement.executeQuery();
                        while (resultSet7.next()) {
                            if (debug) {
                                Utils.info("Ladataan jasenta " + UUID.fromString(resultSet7.getString("uuid")));
                            }
                            new Member(UUID.fromString(resultSet7.getString("uuid")), resultSet7.getString("name"), motiYhteisot.getManager().getOrganization(UUID.fromString(resultSet7.getString("organization"))), motiYhteisot.getManager().getRole(UUID.fromString(resultSet7.getString("role"))), resultSet7.getLong("joined"));
                        }

                        preparedStatement = connection.prepareStatement(sqlType.SELECT_APPLICATION);
                        ResultSet resultSet8 = preparedStatement.executeQuery();
                        while (resultSet8.next()) {
                            if (debug) {
                                Utils.info("Ladataan hakemusta " + UUID.fromString(resultSet8.getString("id")));
                            }
                            new Application(UUID.fromString(resultSet8.getString("id")), UUID.fromString(resultSet8.getString("uuid")), resultSet8.getString("name"), motiYhteisot.getManager().getOrganization(UUID.fromString(resultSet8.getString("organization"))), resultSet8.getString("description"));
                        }
                        if (debug) {
                            Utils.info("Kaikki ladattu onnistuneesti");
                        }

                    } catch (SQLException exception) {
                        exception.printStackTrace();
                    } finally {
                        if (connection != null) {
                            try {
                                connection.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        if (preparedStatement != null) {
                            try {
                                preparedStatement.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        long stop = System.currentTimeMillis();
                        Utils.info("Lataus valmistui " + (stop - start) + "ms");
                        Manager.LOADING = false;

                    }
                }
            }

        }.runTaskAsynchronously(motiYhteisot);
    }
}
