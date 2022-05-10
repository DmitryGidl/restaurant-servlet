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

<div class="container">
    <div class="d-flex justify-content-center h-100">
        <div class="card">
            <div class="card-header">
                <h3><fmt:message key="authorization.singin"/></h3>
            </div>

            <div class="card-body">
                <form method="post" action="<c:url value='/login'/>">
                    <div class="input-group form-group">
                        <div class="input-group-prepend">
                  <span class="input-group-text">
                      <i class="fas fa-user"></i></span>
                        </div>

                        <input
                                type="text"
                                name="email"
                                class="form-control"
                                placeholder=<fmt:message key="login.input.email"/>
                                required
                                autofocus/>
                    </div>

                    <div class="input-group form-group">
                        <div class="input-group-prepend">
                  <span class="input-group-text"
                  ><i class="fas fa-key"></i
                  ></span>
                        </div>
                        <input
                                type="password"
                                name="password"
                                class="form-control"
                                placeholder=<fmt:message key="login.input.password"/>
                                required
                        />
                    </div>
                    <c:if test="${requestScope.error != null}">
                        <div style="color: red">
                            <fmt:message key="${error}"/>
                        </div>
                    </c:if>
                    <div class="row align-items-center remember">
                        <input type="checkbox" name="remember">
                        <fmt:message key="authorization.remember.me"/>
                    </div>
                    <div class="form-group">
                        <input type="submit"
                               value="<fmt:message key="authorization.login"/>"
                               class="btn float-right login_btn"/>
                    </div>
                </form>
            </div>

            <div class="card-footer">
                <div
                        class="d-flex justify-content-center links">
                    <fmt:message key="authorization.signin.question"/>
                </div>
                <c:url value="/signup" var="signUpLink"/>
                <div class="d-flex justify-content-center">
                    <a href=${signUpLink}> <fmt:message key="authorization.signup"/></a>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
