<%@include file="/WEB-INF/jspf/header.jspf" %>

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Foodify Restaurant</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
    <link rel="stylesheet" type="text/css" href="<c:url  value='/css/styleindex.css'/>"/>
</head>
<body>
<div class="container">
    <div class="myHeading text-center text-light pt-4">
        <h1 class="text-light fw-bold m-0 p-0">Foodify Restaurant</h1>
        <c:if test="${sessionScope.user == null}">
        <p class="fw-lighter fst-italic fs-5"><fmt:message key="index.login.header"/></p>
        </c:if>
        <c:if test="${sessionScope.user != null}">
            <p class="fw-lighter fst-italic fs-5"><fmt:message key="index.welcome.header"/>, <span class="text-warning">${sessionScope.user.name}</span></p>
        </c:if>

    </div>
</div>
</body>
</html>