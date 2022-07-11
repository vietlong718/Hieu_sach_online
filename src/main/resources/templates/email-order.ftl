<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>FESHBOOK</title>
</head>
<body>
    <#--Mail order-->
    <div class="container" style="width: 80%; margin-left: 10%; ">
        <header>
            <h1 style="color:aqua; font-size:50px; text-align: center">FRESHBOOK</h1>
        </header>

        <section class="main-content">
            <div class="order-content">
                <p>Ban da dat hang thanh cong, hay xem chi tiet den hang e bang duei:</p>
                <table style="border: gray">
                    <caption align="top" bgcolor="gray" >Thong tin dat hang</caption>
                    <tr>
                        <th bgcolor="gray" style="margin-left: 10px; margin-right: 10px">Ten Khach Hang</th>
                        <td>${user.fullname}</td>
                    </tr>
                    <tr>
                        <th bgcolor="gray" style="margin-left: 10px; margin-right: 10px">Dia Chi</th>
                        <td>${user.address}</td>
                    </tr>
                    <tr >
                        <th bgcolor="gray" style="margin-left: 10px; margin-right: 10px">So Dien Thoai</th>
                        <td>${user.phone}</td>
                    </tr>
                    <tr>
                        <th bgcolor="gray" style="margin-left: 10px; margin-right: 10px">Email</th>
                        <td>${user.email}</td>
                    </tr>
                    <tr >
                        <th bgcolor="gray" style="margin-left: 10px; margin-right: 10px">Ghi Chu</th>
                        <td>${order.note}</td>
                    </tr>
                    <tr >
                        <th bgcolor="gray" style="margin-left: 10px; margin-right: 10px">Ngay Dat Hang</th>
                        <td>
                            ${orderDate}
                        </td>
                    </tr>
                </table>
                <br>
                <table>
                    <caption align="top">Chi tiet den hang</caption>
                    <tr bgcolor="gray">
                        <th style="margin-left: 10px; margin-right: 10px">Ma San Pham</th>
                        <th style="margin-left: 10px; margin-right: 10px">Ten San Pham</th>
                        <th style="margin-left: 10px; margin-right: 10px">Gia</th>
                        <th style="margin-left: 10px; margin-right: 10px">So Luợng</th>
                        <th style="margin-left: 10px; margin-right: 10px">Thanh Tien</th>
                    </tr>
                    <#list books as book>
                        <tr>
                            <td style="margin-left: 10px; margin-right: 10px">${book.book.id}</td>
                            <td style="margin-left: 10px; margin-right: 10px">${book.book.name}</td>
<#--                            <td></td>-->
                            <td style="margin-left: 10px; margin-right: 10px">${book.book.price.price}</td>
                            <td style="margin-left: 10px; margin-right: 10px">${book.quantity}</td>
                            <td style="margin-left: 10px; margin-right: 10px">${book.total}</td>
                        </tr>
                    </#list>
                    <tr>
                        <th style="margin-left: 10px; margin-right: 10px">
                            <td></td>
                            <td></td>
                            <td></td>
                            Tong: ${sumTotal} VND
                        </th>
                    </tr>
                </table>
                <br>
                <p>Thoi gian giao hang du kien tu 3 đen 5 ngay, khong tinh thu 7 va chu nhat.</p>
            </div>
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