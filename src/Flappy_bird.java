import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;



public class Flappy_bird extends JPanel implements ActionListener,KeyListener{
    int boardWidth=1000;
    int boardHeight=630;

    // images
    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;
    int birdX=boardWidth/8+300;
    int birdY=boardHeight/2-100;
    int birdWidth=40;
    int birdHeight=30;
    double score=0;
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

    // game logic
    ArrayList<Pipe> pipes; 
    Bird bird;   
    int velocityX=-4;
    int velocityY=0;
    int gravity=1;
    Timer gameLoop;
    Timer placePipeTimer;
    Random ramdom=new Random();

    boolean gameover=false;

    Flappy_bird(){
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        setBackground(Color.blue);
        setFocusable(true);
        addKeyListener(this);
        //load images
        backgroundImg= new ImageIcon(getClass().getResource("./image.png")).getImage();
        birdImg= new ImageIcon(getClass().getResource("./bird1.png")).getImage();
        topPipeImg= new ImageIcon(getClass().getResource("./R.png")).getImage();
        bottomPipeImg= new ImageIcon(getClass().getResource("./R1.png")).getImage();

        bird=new Bird(birdImg);
        pipes=new ArrayList<Pipe>();
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
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);

    }

    public void draw(Graphics g){
        g.drawImage(backgroundImg, 0, 0, boardWidth,boardHeight,null);

        g.drawImage(birdImg, bird.x, bird.y, bird.width, bird.height, null);

        for(int i=0;i<pipes.size();i++){
            Pipe pipe=pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
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
        if( e.getKeyCode()==KeyEvent.VK_SPACE){
            velocityY=-9;
            if(gameover){
                bird.y=birdY;
                bird.x=birdX;
                score=0;
                pipes.clear();
                velocityY=0;
                gameover=false;
                gameLoop.start();
                placePipeTimer.start();
            }
        }    
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }

}
