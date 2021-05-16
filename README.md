# Manager_zadan

1. Cel projektu:

  Celem projektu jest stworzenie aplikacji na urządzeniu mobilnym za pomocą śródowiska Android Studio, dla kontroli wszystkich ważnych spraw i zadań użytkownika. 
  Aplikacja posiada stronę domową, która zawsze otwiera się w pierwszej kolejności. Widok strony domowej cechuje się swoją prostotą i udogodnieniem, 
  składa się z przycisku po naciśnięciu którego użytkownik przechodzi do widoku stworzenia nowego zadania, oraz menu aplikacji uruchamia się po swajpu w prawo, 
  gdzie zawsze możemy powrócić do głownej strony aplikacji. Projekt ma charakter ważnego „przypomnika”, który ma na celu przechowywać nieskończone cele i zadania użytkownika. 
  
2. Zakres projektu:
  
  Uwzględniając aspekty współczesnego trybu życia, należy wyróżnić jak ważną rolę zajmuję smartphone w dzisiejszych czasach. 
  Smartphony bez żadnych wątpliwości wpływają na bardzo dużą ilośc codziennych procesów i spraw naszego życia: czy to praca, czy to studia, gry, media społecznościowe, 
  poradniki. Dostrzegając ten fakt została wybrana konkretna dziedzina, wymogam której odpowiada ten projekt, wybrano dziedzine poradniki.
  
3. Moduł Strona główna:
   
  ![image](https://user-images.githubusercontent.com/61449911/118397370-46712a00-b65c-11eb-894d-00fca133b197.png)
  ![image](https://user-images.githubusercontent.com/61449911/118397381-538e1900-b65c-11eb-95a9-5ae60cad9d61.png)

  Dana aplikacja do zapisywania danych wykorzystuje bazą danych SQLite, która zapamiętuje wszystkie zadania (datę, godzinę, tekst zadania, czy ono wykonane oraz kolor tła pudełka w którym jest umieszczone zadanie). W samej góry jest przycisk który pokazuje lewe menu aplikacji, oraz kalendar, po naciśnięciu na który możemy wybrać dzień, i sprawdzić czy są zapisane jakieś zadania na ten termin. W dołu widoku jest przycisk dodania nowego zadania.
    
4. Moduł dodania nowego zadania:
  
  ![image](https://user-images.githubusercontent.com/61449911/118397425-83d5b780-b65c-11eb-92cd-33f46e00401f.png)
  
  Dany widok jest stosowany do dodania nowego zadania, są pola do wybrania daty wyświetlanych przez kalendarz, czasu wyświetlanego przez zegarek, napisania treści zadania, oraz wybrania powtórzenia i przepominienia zadania. Także wszystkie pola w razie błedu podanego prez użytkownika weryfikują te dane i w razie pomyłki będzie powiadomienie o tym.
  
5. Moduł edytowania oraz usuwania istniejącego zadania:
  
  ![image](https://user-images.githubusercontent.com/61449911/118397453-b5e71980-b65c-11eb-9621-be1c307c2b02.png)
  
  Ten widok jest prawie taki sam jak widok dodania zadania, tylko wypełniony danymi zadania z bazy danych.
    
6. Wykorzystane technologie:
  
  - Java;
  - XML;
  - Środowisko Android Studio;
