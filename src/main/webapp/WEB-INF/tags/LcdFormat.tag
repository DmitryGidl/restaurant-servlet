<%@ tag import="java.time.LocalDateTime" %>
<%@ tag import="java.time.format.DateTimeFormatter" %>
<%@ attribute name="time" required="true" %>


<% LocalDateTime lcd = LocalDateTime.parse(time);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    out.println( lcd.format(formatter));
%>