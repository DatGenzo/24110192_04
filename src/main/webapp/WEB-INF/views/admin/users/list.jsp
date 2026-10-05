<%@ page
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
%>
<%@ taglib
    prefix="c"
    uri="jakarta.tags.core"
%>

<%@ include file="../../fragments/head.jspf" %>
<%@ include file="../../fragments/admin-header.jspf" %>

<div class="d-flex justify-content-between align-items-center mb-3">
  <div>
    <h1 class="h2 mb-1">Quản lý User</h1>

    <p class="text-muted mb-0">
      Danh sách được phân trang 6 User mỗi trang
    </p>
  </div>

  <a
    class="btn btn-primary"
    href="${pageContext.request.contextPath}/admin/users/create"
  >
    Thêm User
  </a>
</div>

<c:if test="${not empty successMessage}">
  <div class="alert alert-success">
    <c:out value="${successMessage}" />
  </div>
</c:if>

<c:if test="${not empty errorMessage}">
  <div class="alert alert-danger">
    <c:out value="${errorMessage}" />
  </div>
</c:if>

<div class="card shadow-sm">
  <div class="card-header d-flex justify-content-between">
    <span>Danh sách User</span>

    <span class="badge bg-secondary">
      <c:out value="${userPage.totalItems}" /> User
    </span>
  </div>

  <div class="table-responsive">
    <table class="table table-striped table-hover align-middle mb-0">
      <thead class="table-dark">
        <tr>
          <th>Username</th>
          <th>Họ tên</th>
          <th>Email</th>
          <th>Điện thoại</th>
          <th>Quyền</th>
          <th>Trạng thái</th>
          <th class="text-end">Thao tác</th>
        </tr>
      </thead>

      <tbody>
        <c:forEach items="${userPage.items}" var="item">
          <tr>
            <td class="fw-semibold">
              <c:out value="${item.username}" />
            </td>

            <td>
              <c:out value="${item.fullName}" />
            </td>

            <td>
              <c:out value="${item.email}" />
            </td>

            <td>
              <c:out value="${item.phone}" />
            </td>

            <td>
              <c:choose>
                <c:when test="${item.admin}">
                  <span class="badge bg-primary">ADMIN</span>
                </c:when>

                <c:otherwise>
                  <span class="badge bg-secondary">USER</span>
                </c:otherwise>
              </c:choose>
            </td>

            <td>
              <c:choose>
                <c:when test="${item.active}">
                  <span class="badge bg-success">
                    Hoạt động
                  </span>
                </c:when>

                <c:otherwise>
                  <span class="badge bg-danger">
                    Chưa kích hoạt
                  </span>
                </c:otherwise>
              </c:choose>
            </td>

            <td class="text-end">
              <c:url var="editUrl" value="/admin/users/edit">
                <c:param
                  name="username"
                  value="${item.username}"
                />
              </c:url>

              <a
                class="btn btn-warning btn-sm"
                href="${editUrl}"
              >
                Sửa
              </a>

              <form
                class="d-inline"
                method="post"
                action="${pageContext.request.contextPath}/admin/users/delete"
                onsubmit="return confirm('Bạn chắc chắn muốn xóa User này?');"
              >
                <input
                  type="hidden"
                  name="username"
                  value="<c:out value='${item.username}' />"
                />

                <button
                  type="submit"
                  class="btn btn-danger btn-sm"
                >
                  Xóa
                </button>
              </form>
            </td>
          </tr>
        </c:forEach>

        <c:if test="${empty userPage.items}">
          <tr>
            <td colspan="7" class="text-center text-muted py-4">
              Chưa có User
            </td>
          </tr>
        </c:if>
      </tbody>
    </table>
  </div>
</div>

<c:if test="${userPage.totalPages > 1}">
  <nav class="mt-4" aria-label="Phân trang User">
    <ul class="pagination justify-content-center">
      <li class="page-item ${userPage.first ? 'disabled' : ''}">
        <a
          class="page-link"
          href="${pageContext.request.contextPath}/admin/users?page=${userPage.page - 1}"
        >
          Trước
        </a>
      </li>

      <c:forEach
        begin="1"
        end="${userPage.totalPages}"
        var="pageNumber"
      >
        <li
          class="page-item ${pageNumber == userPage.page ? 'active' : ''}"
        >
          <a
            class="page-link"
            href="${pageContext.request.contextPath}/admin/users?page=${pageNumber}"
          >
            <c:out value="${pageNumber}" />
          </a>
        </li>
      </c:forEach>

      <li class="page-item ${userPage.last ? 'disabled' : ''}">
        <a
          class="page-link"
          href="${pageContext.request.contextPath}/admin/users?page=${userPage.page + 1}"
        >
          Sau
        </a>
      </li>
    </ul>
  </nav>
</c:if>

<%@ include file="../../fragments/footer.jspf" %>
