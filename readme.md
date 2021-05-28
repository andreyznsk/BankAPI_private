BANK API v 1.0<br>
Input data  GET<br>
Input data POST - JSON<br>
Output data - JSON<br>
run: sh .startServer.sh
Доступные адреса:
GET:
1. http://localhost:8000/bank_api/get_all_cards  - получить список всех карт
2. http://localhost:8000/bank_api/show_all_accounts - получить список всех счетов
3. http://localhost:8000/bank_api/get_balance_by_card_number/{номер карты} - получить банас счета по номеру карты
4. http://localhost:8000/bank_api/get_card_by_account/{номер карты} - Получить карты по номеру счета
5. http://localhost:8000/bank_api/get_client_by_account_number/{номер счета} - Получить клиента со счетом по номеру счета
POST:
   1.http://localhost:9000/client_api/balance_inc - Пополнить баланс. Нужен JSON объект
   2. http://localhost:9000/client_api/add_card - выпустить новую карту по счету
    
Для тетитьровая ПОСТ запросов можно использовать доп подуль UserServer1.<br>
run: sh .startuser.sh
Доступные адреса:
1. http://localhost:9000/client_api/balance_inc - форма для отправки баланса
2. http://localhost:9000/client_api/add_card - выпустить новую карту по счету