BANK API v 1.0<br>
<h2>Frameworks</h2>
<h3>Module: BankAPI_Spring</h3>
<p>
 - SpringBoot 2.4.5
<br> - Spring Data JPA
<br> - Lombok
<br> - Database H2
<br> - FlyWayDb
<br> - JUnit 5
</p> 
<h3>Module: BankAPIMain</h3>
<p>
- DataSource: Hikari CP
<br> - JavaHttp Server
<br> - JSON - Jackson
<br> - DataBase: H2
<br> - Junit 4
<br> - Cache
</p>
Input data  GET<br>
Input data POST - JSON<br>
Output data - JSON<br>
<p>run: sh .startServer.sh
<h2>Endpoints:</h2>
<h3>GET:</h3>
1. http://localhost:8000/bank_api/get_all_cards  - получить список всех карт
2. http://localhost:8000/bank_api/show_all_accounts - получить список всех счетов
3. http://localhost:8000/bank_api/get_balance_by_card_number/{номер карты} - получить банас счета по номеру карты
4. http://localhost:8000/bank_api/get_card_by_account/{номер карты} - Получить карты по номеру счета
5. http://localhost:8000/bank_api/get_client_by_account_number/{номер счета} - Получить клиента со счетом по номеру счета
   <br>
<h3>POST:</h3>
1. http://localhost:8000/bank_api/balance_inc - Пополнить баланс. Нужен JSON объект
2. http://localhost:8000/bank_api/add_card - выпустить новую карту по счету
    
Для тетитьровая ПОСТ запросов можно использовать доп подуль UserServer1.<br>
run: sh .startuser.sh
Доступные адреса:
1. http://localhost:9000/client_api/balance_inc - форма для отправки баланса
2. http://localhost:9000/client_api/add_card - выпустить новую карту по счету
</br>+ Tests