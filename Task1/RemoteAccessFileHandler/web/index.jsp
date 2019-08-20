<%--
  Created by IntelliJ IDEA.
  User: jch
  Date: 2019-08-18
  Time: 오후 10:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<html>
<head>
    <title>REST API server page</title>
    <link rel="shortcut icon" href="./image/favicon.ico" type="image/favicon.ico">
    <link rel="icon" href="./image/favicon.ico" type="image/favicon.ico">
    <style>
        .comment {
            margin-left: 20px;
        }
        .title span {
            background: linear-gradient(to right, dodgerblue, darkgray);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }
        html, body {
            width : 100%;
            height : 90%;
        }
        header{
            width:750px;
            height:170px;
            position: relative;
            z-index: 20;
            overflow:hidden;
            border-bottom-style: groove;
        }
        section {
            width:750px;
            min-height:90%;
            position: relative;
            z-index:10;
            margin: -170px 0 -50px 0;
            border-top-style: groove;
            border-left-style: groove;
            border-right-style: groove;
        }
        .cont {
            padding-top:175px;
            margin-left: 20px;
        }
        footer {
            width:750px;
            height:35px;
            position:relative;
            z-index: 20;
            overflow:hidden;
            text-align: center;
        }
    </style>
</head>
<body>
    <header>
    <image src="./image/favicon.ico" width="30" height="30" align="left" style="margin-top: 8px;"/>
        <h1 class="title"><span>REST API TEST !!!</span></h1>
        <p class="comment"><b>· This page is for REST API TEST.</b></p>
        <p class="comment"><b>· It sends and receives signals from a computer in the remote area.</b></p>
    </header>
    <section>
        <div class="cont" >
            <b>content</b>
        </div>
    </section>
    <footer style="border-style: groove; padding-top: 10px;">
        <b>COPYRIGHT ⓒ 2019 Changcree ALL RIGHTS RESERVED.</b>
    </footer>
</body>
</html>
