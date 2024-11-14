# Sustainable Foraging

Tech stack: Java, Spring

## Features:

- Add an Item.
- View Items.
- View Foragers.
- Add a Forage.
- View Forages by date.
- Add a Forager.
- Create a report that displays the kilograms of each Item collected on one day.
- Create a report that displays the total value of each Category collected on one day.

## User Stories:

* [x] On startup, the application displays a menu containing at least the following items:
  * Exit
  * View Forages By Date
  * View Foragers By State
  * View Items
  * Add a Forage
  * Add a Forager
  * Add an Item
  * Report: Kilograms of Item
  * Report: Item Category Value
* [x] **Add a Forager** with the following information:
  * First Name: Test
  * Last Name: McTest
  * State: MN
  * Does the application display a success message on the creation of the Forager?
  * Does the application return to the main menu?
  * Open `foragers.csv`. Is the newly added forager present in the file? 
* [x] **Add a Forager** with the same information as above
  * Does the application display an error message that the forager is a duplicate?
  * Does the application return to the main menu?
  * Open `foragers.csv`. Is the duplicate forager present in the file?
* [x] **View Foragers By State** for MN (or **View Foragers By Last Name** for McTest)
  * Does the application display the newly added forager?
* [x] **Add an Item** with the following information:
  * Name: A,B
  * Category: Inedible
  * Price/Kg: 1
  * Does the application do one of the following:
    * Display an error message that Inedible & Poisonous items cannot have a value > 0
    * Add the item successfully but with the value of 0
  * Does the application return to the main menu?
* [x] **Add a Forage** with the following information:
  * Forager: McTest, Test
  * Item Category: Edible
  * Item: Ramps
  * Forage Date: Today + 1 Day
  * Kilograms: 1
  * Does the application display an error message that prevents future forages?
* [x] **Add a Forage** with the following information:
  * Forager: McTest, Test
  * Item Category: Edible
  * Item: Ramps
  * Forage Date: Today
  * Kilograms: 0
  * Does the application display an error message that prevents 0 or negative forage sizes?
* [x] **Add a Forage** with the following information:
  * Forager: McTest, Test
  * Item Category: Edible
  * Item: Ramps
  * Forage Date: Today
  * Kilograms: 1
  * Does the forage successfully add?
  * Have the student open the `forage_data` folder. Is there a new file named `[today's date].csv`?
  * Does that new file have the newly added forage in it?
* [x] **Add a Forage** with the same information that was just successfully added
  * Does the application properly handle duplicate forages (i.e. rejecting them or updatig the existing forage)?
* [x] **View Forages By Date** for Today's Date
  * Does the application display 1 forage?
* [x] **Report: Kilograms of Item** for date 06/26/2020
  * Here is a sampling of data from the original files provided (Note that some of the values may be different if the student added forages for the date in question, but the majority should line up):
  
  Blackberry: 8.85 kg
  Cattail: 6.86 kg
  Chanterelle: 1.94 kg
  Chicken of the Woods Mushroom: 1.03 kg
  Chicory: 20.91 kg
  Dandelion: 7.41 kg
  Duckweed: 13.99 kg
 
* [x] **Report: Item Category Value** for date 06/26/2020
  * Here is the output from the original files provided (Note that some of the values may be different if the student added forages for the date in question, but the majority should line up):
  EDIBLE: $323.06
  MEDICINAL: $141.68
  INEDIBLE: $0.00
