package edu.ufp.esof.order.services.orderoutput;

import edu.ufp.esof.order.models.LineOrder;
import edu.ufp.esof.order.models.OrderItem;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Tc;
import org.docx4j.wml.Tr;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class OrderOutputDocx implements OrderOutput {
    private WordprocessingMLPackage  wordMLPackage;
    {
        try {
            wordMLPackage = WordprocessingMLPackage.createPackage();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    private ObjectFactory factory = Context.getWmlObjectFactory();
    @Override
    public byte[] outputFile (OrderItem order,String type) {
        MainDocumentPart mainDocumentPart = this.wordMLPackage.getMainDocumentPart();
        mainDocumentPart.addStyledParagraphOfText("Title", "Order Details");
        mainDocumentPart.addParagraphOfText("Client: "+order.getClient().getName()+" Address: "+order.getClient().getAddress());

        Tbl table = factory.createTbl();
        addHeader(table);
        for(LineOrder lineOrder:order.getLineOrders()){
            addRow(table,lineOrder);
        }
        wordMLPackage.getMainDocumentPart().addObject(table);
        mainDocumentPart.addParagraphOfText("Total: "+order.price());

        try {
            File file=new File("order"+(order.getId()==null?"null":order.getId())+type);
            wordMLPackage.save(file);
            byte[] toReturn=Files.readAllBytes(file.toPath());
            Files.delete(file.toPath());
            return toReturn;
        } catch (Docx4JException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void addHeader(Tbl table) {
        Tr headerRow = factory.createTr();
        addTableCell(headerRow,"Product Name");
        addTableCell(headerRow,"Quantity");
        addTableCell(headerRow,"Price");
        table.getContent().add(headerRow);
    }

    private void addRow(Tbl table,LineOrder lineOrder) {
        Tr tableRow = factory.createTr();
        addTableCell(tableRow, lineOrder.getProduct().getName());
        addTableCell(tableRow, ""+lineOrder.getQuantity());
        addTableCell(tableRow, ""+lineOrder.getProduct().getPrice());
        table.getContent().add(tableRow);
    }

    private void addTableCell(Tr tableRow, String content) {
        Tc tableCell = factory.createTc();
        tableCell.getContent().add(
                wordMLPackage.getMainDocumentPart().createParagraphOfText(content));
        tableRow.getContent().add(tableCell);
    }
}
