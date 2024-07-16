import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CheckRunner {
	
	//метод создания и записи файла
	static void createFile(String content) {
	try {
      File myObj = new File("Result.csv");
      if (myObj.createNewFile()) {
        System.out.println("File created: " + myObj.getName());
      } else {
        System.out.println("File already exists.");
      }
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
	try {
      FileWriter myWriter = new FileWriter("Result.csv", true);
      myWriter.write(content);
      myWriter.close();
      System.out.println("Successfully wrote to the file.");
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
	}
	
	//метод очистки существующего файла
	static void clearFile(String content) {
	try {
      File myObj = new File("Result.csv");
      if (myObj.createNewFile()) {
        System.out.println("File created: " + myObj.getName());
      } else {
        System.out.println("File already exists.");
      }
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
	try {
      FileWriter myWriter = new FileWriter("Result.csv");
      myWriter.write(content);
      myWriter.close();
      System.out.println("Successfully wrote to the file.");
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
	}


    public static void main(String[] args) {
	
	//проверка входящих аргументов
	Pattern argsValidationPattern = Pattern.compile("^\\[([0-9]*-[0-9]*, ){1,}(discountCard=[0-9]{4}, )?balanceDebitCard=(-)?[0-9]{1,}(\\.[0-9]{2})?\\]$");
    Matcher matcherArgs = argsValidationPattern.matcher(Arrays.toString(args));
	boolean validArgs = matcherArgs.find();
	
	//проверка использования дисконтной карты
	Pattern argsDiscount = Pattern.compile("^\\[([0-9]*-[0-9]*, ){1,}discountCard=[0-9]{4}, balanceDebitCard=(-)?[0-9]{1,}(\\.[0-9]{2})?\\]$");
	Matcher matcherDiscount = argsDiscount.matcher(Arrays.toString(args));
    boolean discountCard = matcherDiscount.find();
	
	//Пустые множества для хранения позиций в чеке
	String[] purchasedProductsId = new String[args.length-1];
	String[] purchasedProductsQuantity = new String[args.length-1];
	
	//списки для хранения данных из products.csv и discountCards.csv		
	ArrayList<String> productsList = new ArrayList<String>();
	ArrayList<String> cardsList = new ArrayList<String>();
		 
	//декларирование переменных:
	int pLinesIndex = 0; //для количества строк products.csv
	int cLinesIndex = 0; //для количества строк discountCards.csv
	int idQuantityPairsNumber = 0; //для количества пар значений продукт-количество во входящих аргументах
		
	//проверка использования дисконта
    if(validArgs) {
      if(discountCard) {
		  idQuantityPairsNumber = args.length - 2;
    } else {
		idQuantityPairsNumber = args.length - 1;
    }
    } else {
      System.out.println("invalid arguments input");
    }

    //заполняет purchasedProductsId[] и purchasedProductsQuantity[]
		 for (int i = 0;
			 i < idQuantityPairsNumber;
			 i = i + 1) {
				 String[] args_values_sep = args[i].split("-");
				 purchasedProductsId[i] = args_values_sep[0];
				 purchasedProductsQuantity[i] = args_values_sep[1];
		 }
	
    //считывает products.csv	
	try {
      BufferedReader reader = new BufferedReader(new FileReader("main\\java\\resources\\products.csv"));
	  String line;
      while((line = reader.readLine()) != null) {
		productsList.add(line);
		pLinesIndex++;
      }
      reader.close();
	
    } catch (IOException e) {
      e.printStackTrace();
    }
	
	//считывает discountCards.csv	
	try {
      BufferedReader reader = new BufferedReader(new FileReader("main\\java\\resources\\discountCards.csv"));
	  String line;
      while((line = reader.readLine()) != null) {
		cardsList.add(line);
		cLinesIndex++;
      }
      reader.close();
	
    } catch (IOException e) {
      e.printStackTrace();
    }
	
	//конвертирует список продуктов во множество
	String[] productsArray = productsList.toArray(new String[pLinesIndex]);
	
	//конвертирует список дисконтных карт во множество
	String[] discountCardsArray = cardsList.toArray(new String[cLinesIndex]);
	
	//создает пустые множества для каждого столбца products.csv
	String[] productsIdArray = new String[pLinesIndex]; //id
	String[] productsDescriptionArray = new String[pLinesIndex]; //description
	String[] productsPriceArray = new String[pLinesIndex]; //price
	String[] productsQuantityArray = new String[pLinesIndex]; //quantity in stock
    String[] productsWholesaleArray = new String[pLinesIndex]; //wholesale product
	
	//создает пустые множества для каждого столбца discountCards.csv
	String[] cardsIdArray = new String[cLinesIndex]; //id
	String[] cardsNumberArray = new String[cLinesIndex]; //number
	String[] cardsDiscountAmountArray = new String[cLinesIndex]; //discount amount
	
	//наполняет множества столбцов products.csv
	for (int i = 0;
	     i < pLinesIndex;
		 i = i + 1) {
			 String[] productLine = productsArray[i].split(",");
			 productsIdArray[i] = productLine[0];
			 productsDescriptionArray[i] = productLine[1];
			 productsPriceArray[i] = productLine[2];
			 productsQuantityArray[i] = productLine[3];
			 productsWholesaleArray[i] = productLine[4];
		 }
		 
	//наполняет множества столбцов discountCards.csv
	for (int i = 0;
	     i < cLinesIndex;
		 i = i + 1) {
			 String[] cardLine = discountCardsArray[i].split(",");
			 cardsIdArray[i] = cardLine[0];
			 cardsNumberArray[i] = cardLine[1];
			 cardsDiscountAmountArray[i] = cardLine[2];
		 }
		 
	//генерирует Result.csv	
	int checkIndexCounter = 0;

    if(discountCard) {
		
		String[] discountCardArray = args[args.length - 2].split("=");
        String discountCardNumber = discountCardArray[1];
	    int existingDiscountIndex = Arrays.asList(cardsNumberArray).indexOf(discountCardNumber); //провереяет номер карты в discauntCards.csv
		
		//если дисконт есть в БД
		if (existingDiscountIndex > 0) {
		System.out.println("\nExisting discount card\n");	
		
	    int discountAmount = Integer.valueOf(cardsDiscountAmountArray[existingDiscountIndex]);
		float [] checkSums = new float[args.length];
		float positionPrice;
		int productIndex;
		int inOrderQuantity;
		String description;
		float price;
		int stockQuantity;
		String wholesale;
		String content;
		float totalPrice;
		
		clearFile(content="");
        
		//генерирует строки позиций
		while(checkIndexCounter < purchasedProductsId.length-1) {
			
	        productIndex = Arrays.asList(productsIdArray).indexOf(purchasedProductsId[checkIndexCounter]);
            inOrderQuantity = Integer.valueOf(purchasedProductsQuantity[checkIndexCounter]);
	        description = productsDescriptionArray[productIndex];
	        price = Float.valueOf(productsPriceArray[productIndex]);
	        stockQuantity = Integer.valueOf(productsQuantityArray[productIndex]);
	        wholesale = productsWholesaleArray[productIndex];
			
			    if (inOrderQuantity <= stockQuantity) {
					
			        //проверяет возможность оптовой скидки на позицию 
                    if(wholesale.equals("+") && inOrderQuantity > 4 && inOrderQuantity <= stockQuantity) {

			            positionPrice = price*inOrderQuantity-price*inOrderQuantity/100*discountAmount;
			            checkSums[checkIndexCounter] = positionPrice;
		                content = description + " " + inOrderQuantity + "pc" + " " + "Price " + price*inOrderQuantity + " Wholesale 10% " + "----" + positionPrice + "\n";	
				        createFile(content);
				        System.out.println(content);
						
			        } else {//если оптовой скидки нет применяет скидку дисконта из БД
                        positionPrice = price*inOrderQuantity-price*inOrderQuantity/100*discountAmount;
				        checkSums[checkIndexCounter] = positionPrice;
		                content = description + " " + inOrderQuantity + "pc" + " " + "Price " + price*inOrderQuantity + " Discount " + discountAmount + "% " + "----" + positionPrice + "\n";
                        System.out.println (content);
				        createFile(content);
			        }
					
		        } else {//в случае если доступное количество превышено
				    positionPrice = 0;
			        checkSums[checkIndexCounter] = positionPrice;
			        content = "!!! Stock quantity exceed for " + description + ". Max available quantity is " + stockQuantity + "\n";
			        createFile(content);
			        System.out.println(content);
			        }
					
			checkIndexCounter++;
			
		}
        //считает и добавляет в чек стоимость всех позиций с примененными скидками
		totalPrice = 0;
		for (int i = 0; i<checkSums.length; i++) {
		totalPrice = totalPrice + checkSums[i];
		}
		content = "\nTotal price ----" + totalPrice;
		createFile(content);
		System.out.println ("Total price ----" + totalPrice);
		}
		
		//если дисконта нет в БД
		if (existingDiscountIndex < 0) {
			
			System.out.println("\nCustom discount card 10%\n");	

			int discountAmount = 10;
			float [] checkSums = new float[args.length];
		    float positionPrice;
			int productIndex;
			int inOrderQuantity;
			String description;
			float price;
			int stockQuantity;
			String wholesale;
			String content;
			float totalPrice;
			
			clearFile(content="");
			
		    while(checkIndexCounter < purchasedProductsId.length-1) {
			
	        productIndex = Arrays.asList(productsIdArray).indexOf(purchasedProductsId[checkIndexCounter]);
            inOrderQuantity = Integer.valueOf(purchasedProductsQuantity[checkIndexCounter]);
	        description = productsDescriptionArray[productIndex];
	        price = Float.valueOf(productsPriceArray[productIndex]);
	        stockQuantity = Integer.valueOf(productsQuantityArray[productIndex]);
	        wholesale = productsWholesaleArray[productIndex];
			positionPrice = price*inOrderQuantity-price*inOrderQuantity/100*discountAmount;
			checkSums[checkIndexCounter] = positionPrice;
			
			    if (inOrderQuantity <= stockQuantity) {
			
                    content = description + " " + inOrderQuantity + "pc" + " " + "Price " + price*inOrderQuantity + " Discount " + discountAmount + "% " + "----" + positionPrice + "\n";
                    System.out.println (content);
                    createFile(content);	
		
			        } else {
						positionPrice = 0;
			            checkSums[checkIndexCounter] = positionPrice;
			            content = "!!! Stock quantity exceed for " + description + ". Max available quantity is " + stockQuantity + "\n";
			            createFile(content);
			            System.out.println(content);
					}
					
			checkIndexCounter++;
			}

        //стоимость всех позиций с примененными скидками
		totalPrice = 0;
		for (int i = 0; i<checkSums.length; i++) {
		totalPrice = totalPrice + checkSums[i];
		}
		System.out.println ("Total price ----" + totalPrice);
		content = "\nTotal price ----" + totalPrice;
		createFile(content);
	}
	} else {
		System.out.println ("\nNo discount card\n");
		
			float [] checkSums = new float[args.length];
		    float positionPrice;
			int productIndex;
			int inOrderQuantity;
			String description;
			float price;
			int stockQuantity;
			String wholesale;
			String content;
			float totalPrice;
			
			clearFile(content="");
			
		    while (checkIndexCounter < purchasedProductsId.length) {
			 
		    //переменные для генерации Result.csv
		    productIndex = Arrays.asList(productsIdArray).indexOf(purchasedProductsId[checkIndexCounter]);
            inOrderQuantity = Integer.valueOf(purchasedProductsQuantity[checkIndexCounter]);
	        description = productsDescriptionArray[productIndex];
	        price = Float.valueOf(productsPriceArray[productIndex]);
	        stockQuantity = Integer.valueOf(productsQuantityArray[productIndex]);
	        wholesale = productsWholesaleArray[productIndex];
			
			    if (inOrderQuantity <= stockQuantity) {
		            if (wholesale.equals("+") && inOrderQuantity > 4 ) {
				
				        positionPrice = price*inOrderQuantity*0.9f;
		                checkSums[checkIndexCounter] = positionPrice;
		                content = description + " " + inOrderQuantity + "pc " + "Price " + price*inOrderQuantity + " Wholesale -10% " + "----" + positionPrice + "\n";	
                        createFile(content);
                        System.out.println (content);					
				    } else {
			          positionPrice = price*inOrderQuantity;
			          checkSums[checkIndexCounter] = positionPrice;
	                  content = description + " " + inOrderQuantity + "pc" + " " + "----" + positionPrice + "\n";
                      createFile(content);
				      System.out.println(content);				  
                    }
				} else {
				    positionPrice = 0;
			        checkSums[checkIndexCounter] = positionPrice;
			        content = "!!! Stock quantity exceed for " + description + ". Max available quantity is " + stockQuantity + "\n";
			        createFile(content);
			        System.out.println(content);
				}
			
            checkIndexCounter++;
	     }
		 
		totalPrice = 0;
		
		for (int i = 0; i<checkSums.length; i++) {
		totalPrice = totalPrice + checkSums[i];
		}
		System.out.println ("Total price ----" + totalPrice);
		content = "\nTotal price ----" + totalPrice;
		createFile(content);
}
}
}