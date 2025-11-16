import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;



public class Flappy_bird extends JPanel implements ActionListener,KeyListener,MouseListener{
    int boardWidth=1000;
    int boardHeight=630;

    // images
    Image backgroundImg;
    Image birdImg;
    Image backgroundImg1;
    Image topPipeImg;
    Image bottomPipeImg;
    Image moveImg;
    Image thach;
    int birdX=boardWidth/8+300;
    int birdY=boardHeight/2-100;
    int birdWidth=40;
    int birdHeight=30;
    double score=0;
    int thachWidth=60;
    int thachHeight=60;
    class Bird{
        int x=birdX;
        int y= birdY;
        int width=birdWidth;
        int height=birdHeight;
        Image img;

        Bird(Image img){
            this.img=img;
        }
    }

    //pipes
    int pipeX=boardWidth;
    int pipeY=0;
    int pipeWidth=64;
    int pipeHeight=600;

    class Pipe{
        int x=pipeX;
        int y=pipeY;
        int width=pipeWidth;
        int height=pipeHeight;
        Image img;
        boolean passed=false;
        Pipe(Image img){
            this.img=img;
        }
    }
    class Meteoroid{
        int x;
        int y;
        int width=thachWidth;
        int height=thachHeight;
        Meteoroid(int x,int y){
            this.x=x;
            this.y=y;
        }
    }
    class spaceMove{
        int x;
        int y;
        int width=40;
        int height=40;
        
        spaceMove(int x,int y){
            
            this.x=x;
            this.y=y;
        }
    }
     

    // game logic
    ArrayList<Pipe> pipes;
    ArrayList<spaceMove> spaceMoves;
    ArrayList<Meteoroid> Meteoroids;
    boolean itemSpawned = false;
    Bird bird;   
    int velocityX=-4;
    int velocityY=0;
    int gravity=1;
    Timer gameLoop;
    Timer placePipeTimer;
    Random random=new Random();

    boolean gameover=false;
    boolean diffcultGame=false;

    Flappy_bird(){
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        setBackground(Color.blue);
        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);
        //load images
        backgroundImg= new ImageIcon(getClass().getResource("./image.png")).getImage();
        backgroundImg1= new ImageIcon(getClass().getResource("./videoframe_10029.png")).getImage();
        birdImg= new ImageIcon(getClass().getResource("./bird1.png")).getImage();
        topPipeImg= new ImageIcon(getClass().getResource("./R.png")).getImage();
        bottomPipeImg= new ImageIcon(getClass().getResource("./R1.png")).getImage();
        moveImg= new ImageIcon(getClass().getResource("./OIP.png")).getImage();
        thach= new ImageIcon(getClass().getResource("./snapedit_1763288240813.png")).getImage();
        bird=new Bird(birdImg);
        pipes=new ArrayList<Pipe>();
        spaceMoves=new ArrayList<spaceMove>();
        Meteoroids=new ArrayList<Meteoroid>();
        //place pipe
        placePipeTimer=new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                placePipes();
            }
        });
        placePipeTimer.start();
        //game loop
        gameLoop= new Timer(1000/60,this);
        gameLoop.start();
    }

    public void placePipes(){
        int ramdomPipeY=(int)(pipeY-pipeHeight/4-Math.random()*(pipeHeight/2));
        Pipe topPipe=new Pipe(topPipeImg);
        int opeingSpace=boardHeight/4;
        topPipe.y=ramdomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe=new Pipe(bottomPipeImg);
        bottomPipe.y=topPipe.y+pipeHeight+opeingSpace;
        pipes.add(bottomPipe);
        int nn=random.nextInt(10);
        if(!diffcultGame &&(nn==0 ||nn==1||nn==2 )){
            int itemX = topPipe.x + 50+pipeWidth;
            int itemY = random.nextInt(boardHeight - 100) + 50;
            spaceMove item = new spaceMove( itemX, itemY);
            spaceMoves.add(item);
        }
        if(diffcultGame){
            int itemX = topPipe.x + 80+pipeWidth;
            int itemY = random.nextInt(boardHeight - 100) + 50;
            Meteoroid item=new Meteoroid(itemX,itemY);
            Meteoroids.add(item);
        }
        
    }

    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);

    }

    public void draw(Graphics g){
        if(diffcultGame){
            g.drawImage(backgroundImg1, 0, 0, boardWidth,boardHeight,null);
        }else{
            g.drawImage(backgroundImg, 0, 0, boardWidth,boardHeight,null);
        }
        

        g.drawImage(birdImg, bird.x, bird.y, bird.width, bird.height, null);
        for(int i=0;i<pipes.size();i++){
            Pipe pipe=pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }
        if(!diffcultGame){
            for(int i=0;i<spaceMoves.size();i++){
                spaceMove gu=spaceMoves.get(i);
                g.drawImage(moveImg, gu.x, gu.y, gu.width, gu.height, null);
            }
        }else{
            for(int i=0;i<Meteoroids.size();i++){
                Meteoroid gu=Meteoroids.get(i);
                g.drawImage(thach, gu.x, gu.y, gu.width, gu.height, null);
            }
        }

                 
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial",Font.PLAIN,40));
        if(gameover){
            g.drawString("Game Over:"+String.valueOf((int)score), 10, 35);
        }else{
             g.drawString(String.valueOf((int)score), 10, 35);
        }
    }

    public void move(){
        velocityY+=gravity;
        bird.y+=velocityY;
        bird.y=Math.max(bird.y,0);

        //pipes
        for(int i=0;i<pipes.size();i++){
            Pipe pipe=pipes.get(i);
            pipe.x+=velocityX;
            if(!pipe.passed && bird.x>pipe.x+pipe.width){
                pipe.passed=true;
                score+=0.5;
            }
            if(collision(bird, pipe)){
                gameover=true;
            }

        }
        for(int i=0;i<spaceMoves.size();i++){
            spaceMove vi=spaceMoves.get(i);
            vi.x+=velocityX;
            if(collision(bird, vi)){
                diffcultGame=true;
            }
        }

        for(int i=0;i<Meteoroids.size();i++){
            Meteoroid vi=Meteoroids.get(i);
            vi.x+=velocityX-2;
            if(collision(bird, vi)){
                gameover=true;
            }
        }
        if(bird.y>boardHeight ){
            gameover=true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameover){
            placePipeTimer.stop();
            gameLoop.stop();
        }
    }
    public boolean collision(Bird a,spaceMove b){

        return a.x<b.x+b.width&&
               a.x+a.width>b.x&&
               a.y<b.y+b.height&&
               a.y+a.height>b.y;
            
    }
    public boolean collision(Bird a,Meteoroid b){

        return a.x<b.x+b.width&&
               a.x+a.width>b.x&&
               a.y<b.y+b.height&&
               a.y+a.height>b.y;
            
    }
    public boolean collision(Bird a,Pipe b){

        return a.x<b.x+b.width&&
               a.x+a.width>b.x&&
               a.y<b.y+b.height&&
               a.y+a.height>b.y;
            

    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if( e.getKeyCode()==KeyEvent.VK_SPACE ){
            jump();
        }    
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        jump();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        jump();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    public void jump(){
        velocityY=-9;
        if(gameover){
            bird.y=birdY;
            bird.x=birdX;
            score=0;
            pipes.clear();
            spaceMoves.clear();
            velocityY=0;
            gameover=false;
            diffcultGame=false;
            Meteoroids.clear();
            gameLoop.start();
            placePipeTimer.start();
        }
    }
}
