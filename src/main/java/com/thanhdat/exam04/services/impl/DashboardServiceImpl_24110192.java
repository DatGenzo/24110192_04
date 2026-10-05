package com.thanhdat.exam04.services.impl;

import java.sql.SQLException;

import com.thanhdat.exam04.daos.DashboardDao_24110192;
import com.thanhdat.exam04.daos.impl.DashboardDaoImpl_24110192;
import com.thanhdat.exam04.models.DashboardStats_24110192;
import com.thanhdat.exam04.services.DashboardService_24110192;

public class DashboardServiceImpl_24110192
        implements DashboardService_24110192 {

    private final DashboardDao_24110192 dashboardDao;

    public DashboardServiceImpl_24110192() {
        this(new DashboardDaoImpl_24110192());
    }

    public DashboardServiceImpl_24110192(
            DashboardDao_24110192 dashboardDao
    ) {
        this.dashboardDao = dashboardDao;
    }

    @Override
    public DashboardStats_24110192 getStats() {
        try {
            return dashboardDao.getStats();
        } catch (SQLException exception) {
            throw new IllegalStateException(
                    "Không thể tải dữ liệu thống kê",
                    exception
            );
        }
    }
}