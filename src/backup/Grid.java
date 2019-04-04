//package backup;
//
//import javafx.scene.layout.GridPane;
//
//public class Grid extends GridPane {
//    Tile[][]liste;
//    int columns;
//    int rows;
//
//
//    public Grid(int rows, int columns){
//        this.columns = columns;
//        this.rows = rows;
//
//        this.liste = new Tile[rows][columns];
//        for(int i=0; i<rows; i++){
//            for(int j=0; j<columns; j++){
//                Tile tile2 = new Tile(GameLogic.tileSize, GameLogic.tileSize);
//                tile2.setTranslateX(j * GameLogic.tileSize);
//                tile2.setTranslateY(i * GameLogic.tileSize);
//                this.liste[i][j] = tile2;
//                this.getChildren().add(tile2);
//            }
//        }
//    }
//
//    public int getColumns(){
//        return columns;
//    }
//
//    public int getRows(){
//        return rows;
//    }
//}