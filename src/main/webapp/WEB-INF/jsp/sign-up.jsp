<%@include file="/WEB-INF/jspf/header.jspf" %>


<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Menu</title>

    <link
            href="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css"
            rel="stylesheet"
            id="bootstrap-css"
    />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU" crossorigin="anonymous"/>
    <!--Fontawesome CDN-->
    <link
            rel="stylesheet"
            href="https://use.fontawesome.com/releases/v5.3.1/css/all.css"
            integrity="sha384-mzrmE5qonljUremFsqc01SB46JvROS7bZs3IO2EmfFsd15uHvIt+Y8vEf7N7fWAU"
            crossorigin="anonymous"
    />
    <!--Custom styles-->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/login.css"/>

</head>
<body>


<div class="d-flex justify-content-center h-100">

    <div class="card signup">
        <div class="card-header">
            <h3><fmt:message key="authorization.signup"/></h3>
        </div>

        <div class="card-body">
            <form action="<c:url value='/signup'/>"
                  method="post">

                <c:if test="${requestScope.errorName != null}">
                    <div style="color: red">
                        <fmt:message key="${requestScope.errorName}"/>
                    </div>
                </c:if>

                <div class="input-group form-group">
                    <div class="input-group-prepend">
                  <span class="input-group-text">
                    <i class="fas fa-user"></i></span>
                    </div>

                    <input type="text"
                           name="name"
                           class="form-control"
                           placeholder="<fmt:message key="singup.input.name"/>"
                           required
                           autofocus/>
                </div>
                <c:if test="${requestScope.errorEmail != null}">
                    <div style="color: red">
                        <fmt:message key="${requestScope.errorEmail}"/>
                    </div>
                </c:if>

                <div class="input-group form-group">
                    <div class="input-group-prepend">
                        <span class="input-group-text"><i class="fas fa-envelope-open"></i></span>
                    </div>

                    <input type="text"
                           name="email"
                           class="form-control"
                           placeholder="<fmt:message key="singup.input.email"/>"
                           required/>
                </div>
                <c:if test="${requestScope.errorPassword != null}">
                    <div style="color: red">
                        <fmt:message key="${requestScope.errorPassword}"/>
                    </div>
                </c:if>
                <c:if test="${requestScope.errorGlobal != null}">
                    <div style="color: red">
                        <fmt:message key="${requestScope.errorGlobal}"/>
                    </div>
                </c:if>

                <div class="input-group form-group">
                    <div class="input-group-prepend">
                  <span class="input-group-text">
                    <i class="fas fa-key"></i></span>
                    </div>

                    <input type="password"
                           name="password"
                           class="form-control" placeholder="<fmt:message key="singup.input.password"/>"
                           required/>
                </div>

                <div class="input-group form-group">
                    <div class="input-group-prepend">
                  <span class="input-group-text">
                    <i class="fas fa-key"></i></span>
                    </div>

                    <input type="password"
                           name="matchingPassword"
                           class="form-control"
                           placeholder="<fmt:message key="singup.input.repeat.password"/>" required/>
                </div>

                <div class="form-group">
                    <input type="submit"
                           value="<fmt:message key="authorization.signup"/>"
                           class="btn float-right login_btn"/>
                </div>
            </form>

        </div>
    </div>
</div>
</body>
</html>
