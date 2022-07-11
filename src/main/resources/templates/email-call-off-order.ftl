<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>FESHBOOK</title>
</head>
<body>
<#--Mail huy don hang-->
<div class="container" style="width: 80%; margin-left: 10%;">
    <header>
        <h1 style="color:aqua; font-size:50px; text-align: center">FRESHBOOK</h1>
    </header>

    <section class="main-content">
        <div class="username">
            <div>Chao ${user.fullname}</div>
        </div>

        <br>
        <p>
            Ban vua huy don hang co id : ${order.id}, voi tong gia tri: ${order.subtotal.price}, vao luc: ${time}
        </p>

    </section>

    <footer style="position: absolute; bottom: 0">
        Ban can tro giup? Lien he voi chung toi
        <br>
        Email: hotro.freshbook@gmail.com
        <br>
        Tel: (+84) 098.542.812
    </footer>
</div>
</body>
</html>