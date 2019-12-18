package edu.ufp.esof.order.services.orderoutput;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import edu.ufp.esof.order.models.LineOrder;
import edu.ufp.esof.order.models.OrderItem;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class OrderOutPutPDF implements OrderOutput {
    @Override
    public byte[] outputFile(OrderItem order,String type) {
        if(order==null){
            return null;
        }
        Document document = new Document();
        try {
            String filename="order"+(order.getId()==null?"null":order.getId())+type;
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();
            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
            document.add(new Paragraph("Client: "+order.getClient().getName()+" Address: "+order.getClient().getAddress(),font));
            PdfPTable table = new PdfPTable(3);
            table.setSpacingBefore(300f);

            addTableHeader(table);
            for(LineOrder lineOrder:order.getLineOrders()){
                table.addCell(lineOrder.getProduct().getName());
                table.addCell(""+lineOrder.getQuantity());
                table.addCell(""+lineOrder.getProduct().getPrice());
            }
            table.setSpacingAfter(300f);
            document.add(table);
            document.add(new Paragraph("Total: €"+order.price(),font));
            document.close();
            byte[] toReturn=Files.readAllBytes(Path.of(filename));
            Files.delete(Path.of(filename));
            return toReturn;
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            return null;
        }


    }
    private void addTableHeader(PdfPTable table) {
        Stream.of("Product ", "Quantity ", "Price")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }
}
