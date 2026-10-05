<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!doctype html>
<html lang="vi">
  <head>
    <%@ include file="../fragments/head.jspf" %>
    <title>Lịch sử đặt hàng</title>
  </head>
  <body class="bg-light d-flex flex-column min-vh-100">
    <%@ include file="../fragments/user-header.jspf" %>
    <main class="container py-4 flex-grow-1">

<div class="d-flex flex-wrap justify-content-between align-items-center gap-3 mb-4">
  <div>
    <h1 class="h2 mb-1">Lịch sử đặt hàng</h1>
    <p class="text-muted mb-0">
      Lọc đơn hàng theo đầy đủ 8 trạng thái xử lý
    </p>
  </div>

  <a
    href="${pageContext.request.contextPath}/videos"
    class="btn btn-primary"
  >
    Mua thêm sản phẩm
  </a>
</div>

<c:if test="${not empty errorMessage}">
  <div class="alert alert-danger">
    <c:out value="${errorMessage}" />
  </div>
</c:if>

<div class="card shadow-sm mb-4">
  <div class="card-body">
    <form
      method="get"
      action="${pageContext.request.contextPath}/orders"
      class="row g-3 align-items-end"
    >
      <div class="col-md-9">
        <label for="status" class="form-label">
          Trạng thái đơn hàng
        </label>
        <select id="status" name="status" class="form-select">
          <option value="">Tất cả trạng thái</option>
          <c:forEach items="${statuses}" var="statusItem">
            <option
              value="${statusItem.code}"
              ${selectedStatus == statusItem.code ? 'selected' : ''}
            >
              <c:out value="${statusItem.label}" />
            </option>
          </c:forEach>
        </select>
      </div>
      <div class="col-md-3 d-grid">
        <button type="submit" class="btn btn-primary">
          Lọc đơn hàng
        </button>
      </div>
    </form>
  </div>
</div>

<c:choose>
  <c:when test="${empty orders}">
    <div class="alert alert-info">
      Không có đơn hàng phù hợp với trạng thái đang chọn.
    </div>
  </c:when>

  <c:otherwise>
    <div class="card shadow-sm overflow-hidden">
      <div class="table-responsive">
        <table class="table table-hover align-middle mb-0">
          <thead class="table-dark">
            <tr>
              <th>Mã đơn</th>
              <th>Ngày đặt</th>
              <th>Người nhận</th>
              <th class="text-center">Số lượng</th>
              <th class="text-end">Tổng tiền</th>
              <th>Thanh toán</th>
              <th>Trạng thái</th>
              <th class="text-end">Chi tiết</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach items="${orders}" var="order">
              <tr>
                <td class="fw-semibold">
                  <c:out value="${order.orderCode}" />
                </td>
                <td>
                  <c:out value="${order.createdAtDisplay}" />
                </td>
                <td>
                  <c:out value="${order.recipientName}" />
                </td>
                <td class="text-center">
                  <c:out value="${order.itemCount}" />
                </td>
                <td class="text-end fw-semibold text-danger">
                  <fmt:formatNumber
                    value="${order.totalAmount}"
                    type="number"
                    groupingUsed="true"
                  /> đ
                </td>
                <td>
                  <span class="badge bg-success">
                    <c:out value="${order.paymentMethod}" />
                  </span>
                </td>
                <td>
                  <span class="badge ${order.status.badgeClass}">
                    <c:out value="${order.status.label}" />
                  </span>
                </td>
                <td class="text-end">
                  <c:url var="detailUrl" value="/orders/detail">
                    <c:param name="id" value="${order.orderId}" />
                  </c:url>
                  <a href="${detailUrl}" class="btn btn-outline-primary btn-sm">
                    Xem
                  </a>
                </td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </div>
    </div>
  </c:otherwise>
</c:choose>

    </main>
    <%@ include file="../fragments/footer.jspf" %>
  </body>
</html>
