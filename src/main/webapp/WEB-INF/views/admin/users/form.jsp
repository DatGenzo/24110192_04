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

<div class="card shadow-sm">
  <div class="card-header">
    <h1 class="h4 mb-0">
      <c:out value="${pageTitle}" />
    </h1>
  </div>

  <div class="card-body">
    <c:if test="${not empty errorMessage}">
      <div class="alert alert-danger">
        <c:out value="${errorMessage}" />
      </div>
    </c:if>

    <form
      method="post"
      action="${pageContext.request.contextPath}${formAction}"
    >
      <div class="mb-3">
        <label for="username" class="form-label">
          Username
        </label>

        <input
          id="username"
          name="username"
          type="text"
          class="form-control"
          value="<c:out value='${userForm.username}' />"
          minlength="3"
          maxlength="50"
          pattern="[A-Za-z0-9._-]+"
          required
          ${creating ? '' : 'readonly'}
        />

        <div class="form-text">
          Username không thể thay đổi sau khi tạo.
        </div>
      </div>

      <div class="mb-3">
        <label for="password" class="form-label">
          ${creating ? 'Mật khẩu' : 'Mật khẩu mới'}
        </label>

        <input
          id="password"
          name="password"
          type="password"
          class="form-control"
          minlength="6"
          ${creating ? 'required' : ''}
        />

        <c:if test="${not creating}">
          <div class="form-text">
            Để trống nếu không muốn thay đổi mật khẩu.
          </div>
        </c:if>
      </div>

      <div class="mb-3">
        <label for="fullName" class="form-label">
          Họ và tên
        </label>

        <input
          id="fullName"
          name="fullName"
          type="text"
          class="form-control"
          value="<c:out value='${userForm.fullName}' />"
          maxlength="100"
          required
        />
      </div>

      <div class="row">
        <div class="col-md-6 mb-3">
          <label for="email" class="form-label">
            Email
          </label>

          <input
            id="email"
            name="email"
            type="email"
            class="form-control"
            value="<c:out value='${userForm.email}' />"
            maxlength="150"
            required
          />
        </div>

        <div class="col-md-6 mb-3">
          <label for="phone" class="form-label">
            Số điện thoại
          </label>

          <input
            id="phone"
            name="phone"
            type="text"
            class="form-control"
            value="<c:out value='${userForm.phone}' />"
            maxlength="20"
          />
        </div>
      </div>

      <div class="mb-3">
        <label for="images" class="form-label">
          Đường dẫn ảnh đại diện
        </label>

        <input
          id="images"
          name="images"
          type="text"
          class="form-control"
          value="<c:out value='${userForm.images}' />"
          maxlength="255"
          placeholder="/assets/images/user.png"
        />
      </div>

      <div class="form-check mb-2">
        <input
          id="admin"
          name="admin"
          type="checkbox"
          class="form-check-input"
          ${userForm.admin ? 'checked' : ''}
        />

        <label for="admin" class="form-check-label">
          Quyền quản trị viên
        </label>
      </div>

      <div class="form-check mb-4">
        <input
          id="active"
          name="active"
          type="checkbox"
          class="form-check-input"
          ${userForm.active ? 'checked' : ''}
        />

        <label for="active" class="form-check-label">
          Tài khoản đang hoạt động
        </label>
      </div>

      <div class="d-flex gap-2">
        <button type="submit" class="btn btn-primary">
          ${creating ? 'Thêm User' : 'Lưu thay đổi'}
        </button>

        <a
          class="btn btn-secondary"
          href="${pageContext.request.contextPath}/admin/users"
        >
          Hủy
        </a>
      </div>
    </form>
  </div>
</div>

<%@ include file="../../fragments/footer.jspf" %>
