package KAGO_framework.view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

/**
 * Diese Klasse dient als vereinfachte Schnittstelle zum Zeichnen. Es handelt sich um eine BlackBox fuer die
 * Graphics2D-Klasse.
 * Vorgegebene Klasse des Frameworks. Modifikation auf eigene Gefahr.
 */
public class DrawTool {

    // Referenzen
    private Graphics2D graphics2D; //java-spezifisches Objekt zum Arbeiten mit 2D-Grafik
    private JComponent parent;

    /**
     * Zeichnet ein Objekt der Klasse BufferedImage
     * @param bI Das zu zeichnende Objekt
     * @param x Die x-Koordinate der oberen linken Ecke
     * @param y Die y-Koordinate der oberen linken Ecke
     */
    public void drawImage(BufferedImage bI, double x, double y){
        if (graphics2D!= null) graphics2D.drawImage(bI, (int)x, (int)y, null);
    }

    /**
     * Zeichnet ein Objekt der Klasse BufferedImage.
     * @param bI das BufferedImage, das gezeichnet wird
     * @param x x-Koordinate der oberen linken Ecke
     * @param y y-Koordinate der oberen linken Ecke
     * @param degrees Grad, die das Bild rotiert sein soll (nicht Bogemaß)
     * @param scale Faktor mit dem das Bild skaliert werden soll (für Wirkung > 0)
     */
    public void drawTransformedImage(BufferedImage bI, double x, double y, double degrees, double scale){
        if (graphics2D!= null){
            AffineTransform transform = new AffineTransform();

            transform.translate(x,y);
            if(scale > 0) {
                transform.scale(scale,scale);
                if(scale < 1){
                    transform.translate(+bI.getWidth() * (1-scale), +bI.getHeight() * (1-scale));
                } else {
                    transform.translate(-bI.getWidth()*(scale-1)*0.25, -bI.getHeight()*(scale-1)*0.25);
                }
            }
            transform.rotate( Math.toRadians(degrees), bI.getWidth()/ (double) 2, bI.getHeight()/ (double) 2 );
            graphics2D.drawImage(bI, transform, null);
        }
    }

    /**
     * Modifiziert die Breite aller gezeichneten Linien
     * Der Standardwert ist 1
     * @param size die gewünschte Breite in Pixeln
     */
    public void setLineWidth(int size){
        if (graphics2D!= null){
            graphics2D.setStroke(new BasicStroke(size));
        }
    }

    /**
     * Zeichnet ein Rechteck als Linie ohne Fuellung
     * @param x Die x-Koordinate der oberen linken Ecke
     * @param y Die y-Koordinate der oberen linken Ecke
     * @param width Die Breite
     * @param height Die Hoehe
     */
    public void drawRectangle(double x, double y, double width, double height){
        Rectangle2D.Double r = new Rectangle2D.Double(x,y,width,height);
        if (graphics2D!= null) graphics2D.draw(r);
    }

    /**
     * Zeichnet ein Quadrat als Linie ohne Fuellung
     * Credit: Nils Derenthal
     * @param x Die x-Koordinate der oberen linken Ecke
     * @param y Die y-Koordinate der oberen linken Ecke
     * @param sideLength die Länge einer Seite des Quadrats
     */
    public void drawRectangle(double x, double y, double sideLength){
        drawRectangle(x,y,sideLength,sideLength);

    }

    /**
     * Zeichnet ein gefuelltes Rechteck
     * @param x Die x-Koordinate der oberen linken Ecke
     * @param y Die y-Koordinate der oberen linken Ecke
     * @param width Die Breite
     * @param height Die Hoehe
     */
    public void drawFilledRectangle(double x, double y, double width, double height){
        Rectangle2D.Double r = new Rectangle2D.Double(x,y,width,height);
        if (graphics2D!= null) graphics2D.fill(r);
    }

    /**
     * Aendert die aktuell verwendete Farbe zum Zeichnen auf eine andere Farbe.
     * Die aenderung gilt solange, bis eine neue Farbe gesetzt wird (Zustand)
     * @param r Der Gruen-Anteil der Farbe (0 bis 255)
     * @param g Der Gelb-Anteil der Farbe (0 bis 255)
     * @param b Der Blau-Anteil der Farbe (0 bis 255)
     * @param alpha Die Transparenz der Farbe, wobei 0 nicht sichtbar und 255 voll deckend ist
     */
    public void setCurrentColor(int r, int g, int b, int alpha){
        if (graphics2D!= null) {
            graphics2D.setColor( new Color(r,g,b,alpha) );
        }
    }

    /**
     * Aendert die aktuell verwendete Farbe zum Zeichnen auf eine andere Farbe.
     * Die Änderung gilt solange, bis eine neue Farbe gesetzt wird.
     * @param color Von außen festgelegtes Farb-Objekt
     */
    public void setCurrentColor(Color color){
        if (graphics2D!= null && color != null) graphics2D.setColor( color );
    }

    /**
     * Aendert die aktuell verwendete Farbe zum Zeichnen auf eine andere Farbe.
     * Die aenderung gilt solange, bis eine neue Farbe gesetzt wird (Zustand)
     * Credit: Nils Derenthal
     * @param h Der Farbton, dargestellt in einem Farbring in Grad (0-360)
     *         -> https://crosstownarts.org/wp-content/uploads/2019/11/colour-wheel-1740381_1920-1200x1200.jpg
     * @param s Die Sättigung der Farbe in Prozent (0-100)
     * @param b Die Helligkeit der Farbe in Prozent (0-100)
     */
    public void setCurrentHSBColor(float h, float s, float b){
        if (graphics2D!= null) graphics2D.setColor( Color.getHSBColor(h,s,b));
    }

    /**
     * Zeichnet einen Kreis ohne Fuellung als Linie
     * @param x Die x-Koordinate des Mittelpunkts
     * @param y Die y-Koordinate des Mittelpunkts
     * @param radius Der Kreisradius
     */
    public void drawCircle(double x, double y, double radius){
        Ellipse2D.Double e = new Ellipse2D.Double(x-radius,y-radius,radius*2,radius*2);
        if (graphics2D!= null) graphics2D.draw(e);
    }

    /**
     * Zeichnet einen gefuellten Kreis
     * @param x Die x-Koordinate des Mittelpunkts
     * @param y Die y-Koordinate des Mittelpunkts
     * @param radius Der Kreisradius
     */
    public void drawFilledCircle(double x, double y, double radius){
        Ellipse2D.Double e = new Ellipse2D.Double(x-radius,y-radius,radius*2,radius*2);
        if (graphics2D!= null) graphics2D.fill(e);
    }

    /**
     * Zeichnet ein nicht gefuelltes Dreieck
     * @param x1 Die x-Koordinate der ersten Ecke
     * @param y1 Die y-Koordinate der ersten Ecke
     * @param x2 Die x-Koordinate der zweiten Ecke
     * @param y2 Die y-Koordinate der zweiten Ecke
     * @param x3 Die x-Koordinate der dritten Ecke
     * @param y3 Die y-Koordinate der dritten Ecke
     */
    public void drawTriangle(double x1, double y1, double x2, double y2, double x3, double y3 ){
        drawPolygon(x1,y1,x2,y2,x3,y3);
    }

    /**
     * Zeichnet ein gefuelltes Dreieck
     * @param x1 Die x-Koordinate der ersten Ecke
     * @param y1 Die y-Koordinate der ersten Ecke
     * @param x2 Die x-Koordinate der zweiten Ecke
     * @param y2 Die y-Koordinate der zweiten Ecke
     * @param x3 Die x-Koordinate der dritten Ecke
     * @param y3 Die y-Koordinate der dritten Ecke
     */
    public void drawFilledTriangle(double x1, double y1, double x2, double y2, double x3, double y3 ){
        drawFilledPolygon(x1,y1,x2,y2,x3,y3);
    }

    /**
     * Zeichnet ein Polygon mit beliebig vielen Eckpunkten (Mehr als 3).
     * @param eckpunkte eine gerade anzahl an Ecken des Polygons. Diese folgen dem Schema: [[x1], [y1], [x2], [y2], [x1]] etc.
     * @author Nils Derenthal
     */
    public void drawPolygon (double ... eckpunkte) {
        graphics2D.draw(getPolygon(eckpunkte));
    }

    /**
     * Zeichnet ein gefülltes Polygon mit beliebig vielen Eckpunkten (Mehr als 3).
     * @param eckpunkte eine gerade anzahl an Ecken des Polygons. Diese folgen dem Schema: [[x1], [y1], [x2], [y2], [x1]] etc.
     * @author Nils Derenthal
     */
    public void drawFilledPolygon (double ... eckpunkte) {
        graphics2D.fill(getPolygon(eckpunkte));
    }

    /**
     * Helper funktion für doppelten Code-block um ein Polygon aus einem Array von Ecken zu erzeugen.
     * @param eckPunkte eine gerade anzahl an Ecken des Polygons. Diese folgen dem Schema: [[x1], [y1], [x2], [y2], [x1]] etc.
     * @return das durch die Eckpunkte geschaffene Polygon
     * @author Nils Derenthal
     */
    private Polygon getPolygon(double[] eckPunkte) {
        //garantiert das eine gerade anzahl an ecken vorhanden ist und das es mehr als 3 ecken sind
        assert eckPunkte.length % 2 == 0 && eckPunkte.length >= 3 * 2;
        assert graphics2D != null;

        Polygon p = new Polygon();

        for (int i = 0; i < eckPunkte.length - 1; i += 2) {
            p.addPoint ((int)eckPunkte[i], (int) eckPunkte[i + 1]);
        }
        return p;
    }

    /**
     * Zeichnet eine duenne Linie zwischen den beiden Punkten
     * @param x1 Die x-Koordinate des ersten Punkts
     * @param y1 Die y-Koordinate des ersten Punkts
     * @param x2 Die x-Koordinate des zweiten Punkts
     * @param y2 Die y-Koordinate des zweiten Punkts
     */
    public void drawLine(double x1, double y1, double x2, double y2){
        Line2D.Double line = new Line2D.Double(x1,y1,x2,y2);
        if (graphics2D!= null) graphics2D.draw(line);
    }

    /**
     * Zeichnet eine Ellipse ohne Fuellung als Linie
     * Credit: Nils Derenthal
     * @param x Die x-Koordinate des Mittelpunkts in X-Richtung
     * @param y Die y-Koordinate des Mittelpunkts in Y-Richtung
     * @param radiusX Der Kreisradius in X-Richtung
     * @param radiusY Der KreisRadius in Y-Richtung
     */
    public void drawEllipse (double x, double y, double radiusX, double radiusY){
        Ellipse2D.Double e = new Ellipse2D.Double(x-radiusX,y-radiusY,radiusX*2,radiusY*2);
        if (graphics2D!= null) graphics2D.draw(e);
    }

    /**
     * Zeichnet eine gefuellte Ellipse
     * Credit: Nils Derenthal
     * @param x Die x-Koordinate des Mittelpunkts in X-Richtung
     * @param y Die y-Koordinate des Mittelpunkts in Y-Richtung
     * @param radiusX Der Kreisradius in X-Richtung
     * @param radiusY Der KreisRadius in Y-Richtung
     */
    public void drawFilledEllipse (double x, double y, double radiusX, double radiusY){
        Ellipse2D.Double e = new Ellipse2D.Double(x-radiusX,y-radiusY,radiusX*2,radiusY*2);
        if (graphics2D!= null) graphics2D.fill(e);
    }

    /**
     * Zeichnet einen Kreisausschnitt als Linie ohne Füllung
     * Credit: Nils Derenthal
     * @param x Die X-Koordinate des Mittelpunktes des Kreisausschnitts
     * @param y Die Y-Koordinate des Mittelpunktes des Kreisausschnitts
     * @param radius Der Radius des Kreisausschnittes
     * @param startingAngle Der Anfangswinkel des Kreisausschnitts (0° entspricht dem rechtesten Punkt des Kreises)
     * @param endingAngle Der Endwinkel des Kreisauschnitts
     * @param type Der Typ des Kreisausschnitt.
     * -> 0 entspricht offen, 1 entspricht einer Linie zwischen den beiden Winkeln, 2 entspricht Linien zwischen dem Mittelpunkt und den Winkeln
     */
    public void drawArc(double x, double y, double radius, double startingAngle, double endingAngle, int type){
        if (type > 2)  throw new IllegalArgumentException("must be in a 0 - 2 scope");
        Arc2D.Double arc = new Arc2D.Double(x,y,radius,radius,startingAngle,endingAngle,type);
        if (graphics2D!= null) graphics2D.draw(arc);
    }

    /**
     * Zeichnet einen gefuellten Kreisausschnitt
     * Credit: Nils Derenthal
     * @param x Die X-Koordinate des Mittelpunktes des Kreisausschnitts
     * @param y Die Y-Koordinate des Mittelpunktes des Kreisausschnitts
     * @param radius Der Radius des Kreisausschnittes
     * @param startingAngle Der Anfangswinkel des Kreisausschnitts (0° entspricht dem rechtesten Punkt des Kreises)
     * @param endingAngle Der Endwinkel des Kreisauschnitts
     * @param type Der Typ des Kreisausschnitt.
     * -> 0 entspricht offen, 1 entspricht einer Linie zwischen den beiden Winkeln, 2 entspricht Linien zwischen dem Mittelpunkt und den Winkeln
     */
    public void drawFilledArc(double x, double y, double radius, double startingAngle, double endingAngle, int type){
        if (type > 2)  throw new IllegalArgumentException("must be in a 0 - 2 scope");
        Arc2D.Double arc = new Arc2D.Double(x,y,radius,radius,startingAngle,endingAngle,type);
        if (graphics2D!= null) graphics2D.fill(arc);
    }

    /**
     * Zeichnet einen Kreisausschnitt als Linie ohne Füllung
     * Credit: Nils Derenthal
     * @param x Die X-Koordinate des Mittelpunktes des Kreisausschnitts
     * @param y Die Y-Koordinate des Mittelpunktes des Kreisausschnitts
     * @param radiusX Der Radius des Kreisausschnittes in X-Richtung
     * @param radiusY Der Radius des Kreisausschnittes in Y-Richtung
     * @param startingAngle Der Anfangswinkel des Kreisausschnitts (0° entspricht dem rechtesten Punkt des Kreises)
     * @param endingAngle Der Endwinkel des Kreisauschnitts
     * @param type Der Typ des Kreisausschnitt.
     * -> 0 entspricht offen, 1 entspricht einer Linie zwischen den beiden Winkeln, 2 entspricht Linien zwischen dem Mittelpunkt und den Winkeln
     */
    public void drawEllipticArc(double x, double y, double radiusX, double radiusY, double startingAngle, double endingAngle, int type){
        if (type > 2)  throw new IllegalArgumentException("must be in a 0 - 2 scope");
        Arc2D.Double arc = new Arc2D.Double(x,y,radiusX,radiusY,startingAngle,endingAngle,type);
        if (graphics2D!= null) graphics2D.draw(arc);
    }

    /**
     * Zeichnet einen gefuellten Kreisausschnitt
     * Credit: Nils Derenthal
     * @param x Die X-Koordinate des Mittelpunktes des Kreisausschnitts
     * @param y Die Y-Koordinate des Mittelpunktes des Kreisausschnitts
     * @param radiusX Der Radius des Kreisausschnittes in X-Richtung
     * @param radiusY Der Radius des Kreisausschnittes in Y-Richtung
     * @param startingAngle Der Anfangswinkel des Kreisausschnitts (0° entspricht dem rechtesten Punkt des Kreises)
     * @param endingAngle Der Endwinkel des Kreisauschnitts
     * @param type Der Typ des Kreisausschnitt.
     * -> 0 entspricht offen, 1 entspricht einer Linie zwischen den beiden Winkeln, 2 entspricht Linien zwischen dem Mittelpunkt und den Winkeln
     */
    public void drawFilledEllipticArc(double x, double y, double radiusX, double radiusY, double startingAngle, double endingAngle, int type){
        if (type > 2)  throw new IllegalArgumentException("must be in a 0 - 2 scope");
        Arc2D.Double arc = new Arc2D.Double(x,y,radiusX,radiusY,startingAngle,endingAngle,type);
        if (graphics2D!= null) graphics2D.fill(arc);
    }

    /**
     * Zeichnet einen Text an die gewuenschte Stelle
     * @param x Die x-Position des Textbeginns
     * @param y Die y-Position des Textbeginns
     * @param text Der anzuzeigende Text
     */
    public void drawText(double x, double y, String text){
        if (graphics2D!=null) graphics2D.drawString(text,(int)x,(int)y);
    }

    /**
     * Passt die Schriftart und Größe der gezeichneten Texte an.
     * @param fontName Der Name der Schriftart, z.B. "Arial"
     * @param style Der Style für die Schriftart, z.B. Font.BOLD
     * @param size Die Größe der Schrift
     */
    public void formatText(String fontName, int style, int size){
        if (graphics2D != null) graphics2D.setFont(new Font(fontName, style, size));
    }
	
    /**
     * Spezifiziert das zu verwendende Grafikobjekt von Java und
     * das Objekt in dem gezeichnet wird.
     * Bitte nicht verwenden.
     * @param g2d Das java-interne Grafikobjekt des Programmfensters
     */
    public void setGraphics2D(Graphics2D g2d, JComponent parent){
        graphics2D = g2d;
        this.parent = parent;
    }

    public Graphics2D getGraphics2D(){
        return graphics2D;
    }

    public JComponent getParent(){
        return parent;
    }

}
