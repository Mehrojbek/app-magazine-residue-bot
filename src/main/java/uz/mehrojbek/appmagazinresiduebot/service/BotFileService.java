package uz.mehrojbek.appmagazinresiduebot.service;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.mehrojbek.appmagazinresiduebot.entity.Product;
import uz.mehrojbek.appmagazinresiduebot.entity.enums.BranchTypeEnum;
import uz.mehrojbek.appmagazinresiduebot.repository.ProductRepository;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BotFileService {
    private final ProductRepository productRepository;

    public SendMessage saveProducts(Message message, String name) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));

        try {
            List<Product> products = new ArrayList<>();
            //obtaining input bytes from a file
            FileInputStream fis = new FileInputStream("downloads/" + name);
            //creating workbook instance that refers to .xls file
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            //creating a Sheet object to retrieve the object
            XSSFSheet sheet = wb.getSheetAt(0);
            //evaluating cell type
            for (Row row : sheet)     //iteration over row using for each loop
            {
                int emptyCells = 0;
                Product product = new Product();
                if (row.getRowNum() > 1) {
                    for (Cell cell : row) {
                        int columnIndex = cell.getColumnIndex();

                        if (columnIndex > 11) {
                            if (emptyCells < 4) {
                                products.add(product);
                            }
                            break;
                        }
                        CellType cellType = cell.getCellType();
                        if ((cellType.equals(CellType._NONE) || cellType.equals(CellType.BLANK))) {
                            emptyCells++;
                            continue;
                        }

                        switch (columnIndex) {
                            case 0:
                                try {
                                    String stringCellValue = cell.toString();
                                    if (stringCellValue.contains("Дукон")) {
                                        product.setBranchType(BranchTypeEnum.MAGAZINE);
                                    } else {
                                        product.setBranchType(BranchTypeEnum.WAREHOUSE);
                                    }
                                } catch (Exception e) {
                                    String response = "1-ustun    " + row.getRowNum()+1 + " - qatorda xatolik bo'ldi " + "\n bu katakda ushbu qiymat kelgan:" + cell +"   xatolik nomi:"+ e.getMessage();
                                    sendMessage.setText(response);
                                    return sendMessage;
                                }
                                break;
                            case 1:
                                try {
                                    String type = cell.toString();
                                    product.setType(type);
                                }catch (Exception e){
                                    String response = "2-ustun    " + row.getRowNum()+1 + " - qatorda xatolik bo'ldi " + "\n bu katakda ushbu qiymat kelgan:" + cell +"   xatolik nomi:"+ e.getMessage();
                                    sendMessage.setText(response);
                                    return sendMessage;
                                }
                                break;
                            case 2:
                                try {
                                    String brand = cell.toString();
                                    product.setBrand(brand);
                                }catch (Exception e){
                                    String response = "3-ustun    " + row.getRowNum()+1 + " - qatorda xatolik bo'ldi " + "\n bu katakda ushbu qiymat kelgan:" + cell +"   xatolik nomi:"+ e.getMessage();
                                    sendMessage.setText(response);
                                    return sendMessage;
                                }
                                break;
                            case 3:
                                try {
                                    String productName = cell.toString();
                                    product.setName(productName);
                                }catch (Exception e){
                                    String response = "4-ustun    " + row.getRowNum()+1 + " - qatorda xatolik bo'ldi " + "\n bu katakda ushbu qiymat kelgan:" + cell +"   xatolik nomi:"+ e.getMessage();
                                    sendMessage.setText(response);
                                    return sendMessage;
                                }
                                break;
                            case 4:
                                try {
                                    String tariff = cell.toString();
                                    product.setTariff(tariff);
                                }catch (Exception e){
                                    String response = "5-ustun    " + row.getRowNum()+1 + " - qatorda xatolik bo'ldi " + "\n bu katakda ushbu qiymat kelgan:" + cell +"   xatolik nomi:"+ e.getMessage();
                                    sendMessage.setText(response);
                                    return sendMessage;
                                }
                                break;
                            case 5:
                                try {
                                    double size = Double.parseDouble(cell.toString());
                                    product.setSize(size);
                                } catch (Exception e) {
                                    String response = "6-ustun    " + row.getRowNum()+1 + " - qatorda xatolik bo'ldi " + "\n bu katakda ushbu qiymat kelgan:" + cell +"   xatolik nomi:"+ e.getMessage();
                                    sendMessage.setText(response);
                                    return sendMessage;
                                }
                                break;
                            case 6:
                                try {
                                    double residue = Double.parseDouble(cell.toString());
                                    product.setResidue(residue);
                                } catch (Exception e) {
                                    String response = "7-ustun    " + row.getRowNum()+1 + " - qatorda xatolik bo'ldi " + "\n bu katakda ushbu qiymat kelgan:" + cell +"   xatolik nomi:"+ e.getMessage();
                                    sendMessage.setText(response);
                                    return sendMessage;
                                }
                                break;
                            case 7:
                                try {
                                    String measurement = cell.toString();
                                    product.setMeasurement(measurement);
                                } catch (Exception e) {
                                    String response = "8-ustun    " + row.getRowNum()+1 + " - qatorda xatolik bo'ldi " + "\n bu katakda ushbu qiymat kelgan:" + cell +"   xatolik nomi:"+ e.getMessage();
                                    sendMessage.setText(response);
                                    return sendMessage;
                                }
                                break;
                            case 8:
                                try {
                                    String currency = cell.toString();
                                    product.setCurrency(currency);
                                } catch (Exception e) {
                                    String response = "9-ustun    " + row.getRowNum()+1 + " - qatorda xatolik bo'ldi " + "\n bu katakda ushbu qiymat kelgan:" + cell +"   xatolik nomi:"+ e.getMessage();
                                    sendMessage.setText(response);
                                    return sendMessage;
                                }
                                break;
                            case 9:
                                try {
                                    double usd = Double.parseDouble(cell.toString());
                                    product.setPriceUsd(usd);
                                } catch (Exception e) {
                                    String response = "10-ustun    " + row.getRowNum()+1 + " - qatorda xatolik bo'ldi " + "\n bu katakda ushbu qiymat kelgan:" + cell +"   xatolik nomi:"+ e.getMessage();
                                    sendMessage.setText(response);
                                    return sendMessage;
                                }
                                break;
                            case 10:
                                try {
                                    double som = Double.parseDouble(cell.toString());
                                    product.setPriceSom(som);
                                } catch (Exception e) {
                                    String response = "11-ustun    " + row.getRowNum()+1 + " - qatorda xatolik bo'ldi " + "\n bu katakda ushbu qiymat kelgan:" + cell +"   xatolik nomi:"+ e.getMessage();
                                    sendMessage.setText(response);
                                    return sendMessage;
                                }
                                break;
                            case 11:
                                try {
                                    double sumPrice = Double.parseDouble(cell.toString());
                                    product.setSumPrice(sumPrice);
                                } catch (Exception e) {
                                    String response = "12-ustun    " + row.getRowNum()+1 + " - qatorda xatolik bo'ldi " + "\n bu katakda ushbu qiymat kelgan:" + cell +"   xatolik nomi:"+ e.getMessage();
                                    sendMessage.setText(response);
                                    return sendMessage;
                                }
                                break;
                        }
                    }
                }
            }
            productRepository.deleteAll();
            productRepository.saveAll(products);
            sendMessage.setText("Ma'lumotlar omboriga bemorlar ro'yxati qo'shildi");
            return sendMessage;
        } catch (Exception e) {
            e.printStackTrace();
            sendMessage.setText("Xatolik yuz berdi");
            return sendMessage;
        }
    }
}
