package com.thanhdat.exam04.daos;

import java.sql.SQLException;

import com.thanhdat.exam04.models.DashboardStats_24110192;

public interface DashboardDao_24110192 {

    DashboardStats_24110192 getStats()
            throws SQLException;
}