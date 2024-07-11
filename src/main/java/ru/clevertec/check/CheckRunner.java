import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CheckRunner {
	//метод создания файла
	public void createFile(String content) {
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
	
	
	// Расчет стоимости позиции без дисконтной карты
    public void PriceWithoutDiscount(float price, int inOrderQuantity, String wholesale) {
    if(wholesale.equals("+") && inOrderQuantity > 4) {
		  System.out.println("Wholesale price " + price*inOrderQuantity*0.9);	
    } else {
		
	    System.out.println("The price is " + price*inOrderQuantity);	
      };
	}
	
	// Расчет стоимости позиции с дисконтной картой
    public void PriceWithDiscount(float price, int inOrderQuantity, String wholesale,int discountAmount) {
    if(wholesale.equals("+") && inOrderQuantity > 4) {
		discountAmount = 10;
		  System.out.println("Wholesale price " + (price*inOrderQuantity-price*inOrderQuantity/100*discountAmount) + " Discount amount " + discountAmount);	
    } else {
		System.out.println("Discount price " + (price*inOrderQuantity-price*inOrderQuantity/100*discountAmount));	
    };
	}

    public static void main(String[] args) {
		
	//списки для хранения данных из products.csv и discountCards.csv		
	ArrayList<String> productsList = new ArrayList<String>();
	ArrayList<String> cardsList = new ArrayList<String>();
	
	//Пустые множества для хранения позиций в чеке
	String[] purchasedProductsId = new String[args.length];
	String[] purchasedProductsQuantity = new String[args.length];
		 
	//декларирование переменных:
	int pLinesIndex = 0; //переменная количества строк в products.csv
	int cLinesIndex = 0; //переменная количества строк в discountCards.csv
	int idQuantityPairsNumber = 0; //переменная количества пар значений продукт-количество во входящих аргументах
	int withDiscount = 0;
		
	//проверка входящих аргументов
	Pattern argsValidationPattern = Pattern.compile("^\\[([0-9]*-[0-9]*, ){1,}(discountCard=[0-9]{4}, )?balanceDebitCard=(-)?[0-9]{1,}(\\.[0-9]{2})?\\]$");
    Matcher matcherArgs = argsValidationPattern.matcher(Arrays.toString(args));
	boolean validArgs = matcherArgs.find();
	
	//проверка использования дисконтной карты
	Pattern argsDiscount = Pattern.compile("^\\[([0-9]*-[0-9]*, ){1,}discountCard=[0-9]{4}, balanceDebitCard=(-)?[0-9]{1,}(\\.[0-9]{2})?\\]$");
	Matcher matcherDiscount = argsDiscount.matcher(Arrays.toString(args));
    boolean discountCard = matcherDiscount.find();
	
	//определение сценария
    if(validArgs) {
      if(discountCard) {
		  idQuantityPairsNumber = args.length - 2;
    } else {
		idQuantityPairsNumber = args.length - 1;
    };
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
				 System.out.println("Product id " + args_values_sep[0] + " in quantity " + args_values_sep[1]);
		 }
	
    //считывание products.csv	
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
	
	//считывание discountCards.csv	
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
	
	
	//конвертирование списка продуктов во множество
	String[] productsArray = productsList.toArray(new String[pLinesIndex]);
	
	//конвертирование списка дисконтных карт во множество
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
	
	//наполняет созданные множества столбцов products.csv
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
		 
	//наполняет созданные множества столбцов discountCards.csv
	for (int i = 0;
	     i < cLinesIndex;
		 i = i + 1) {
			 String[] cardLine = discountCardsArray[i].split(",");
			 cardsIdArray[i] = cardLine[0];
			 cardsNumberArray[i] = cardLine[1];
			 cardsDiscountAmountArray[i] = cardLine[2];
		 }
		 
	//генерирует Result.csv
	//BufferedWriter br = new BufferedWriter(new FileWriter(new File("abc.txt")));
    //br.write("some text");
	
    //переменная индекса для последовательного вывода позиций в чек
	int checkIndexCounter = 0;
	
	//переменные для генерации Result.csv
	int productIndex = Arrays.asList(productsIdArray).indexOf(purchasedProductsId[checkIndexCounter]);
    int inOrderQuantity = Arrays.asList(productsIdArray).indexOf(purchasedProductsQuantity[checkIndexCounter]);
	String description = productsDescriptionArray[productIndex];
	float price = Float.valueOf(productsPriceArray[productIndex]);
	int stockQuantity = Integer.valueOf(productsQuantityArray[productIndex]);
	String wholesale = productsWholesaleArray[productIndex];
    
	
	
    try {
		String[] discountCardArray = args[args.length - 2].split("=");
    String discountCardNumber = discountCardArray[1];
	int existingDiscountIndex = Arrays.asList(cardsNumberArray).indexOf(discountCardNumber); //провереяет номер карты в discauntCards.csv
		int discountAmount = Integer.valueOf(cardsDiscountAmountArray[existingDiscountIndex]);
		System.out.println (description + " price " + price + " quantity " + inOrderQuantity);
	    CheckRunner priceCalc = new CheckRunner();
        priceCalc.PriceWithDiscount(price,inOrderQuantity,wholesale,discountAmount);
	    System.out.println("discount from csv");
	}
	catch(Exception e) {
		String[] discountCardArray = args[args.length - 2].split("=");
        try {
		String discountCardNumber = discountCardArray[1];
		int discountAmount = 10;
		System.out.println ("custom discount");
		System.out.println (description + " price " + price + " quantity " + inOrderQuantity + " discountAmount " + discountAmount);
		
		CheckRunner priceCalc = new CheckRunner();
        priceCalc.PriceWithDiscount(price,inOrderQuantity,wholesale,discountAmount);
		}
		catch (Exception f) {
			System.out.println ("no discount");
			System.out.println (description + " price " + price + " quantity " + inOrderQuantity);
			
			CheckRunner priceCalc = new CheckRunner();
            priceCalc.PriceWithoutDiscount(price,inOrderQuantity,wholesale);
		}
	}
	}
}