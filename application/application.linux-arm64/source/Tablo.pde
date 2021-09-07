
int age = 0;


ArrayList<PVector>[] kolBoot = new ArrayList[kolType];
int kolB[] = new int[kolType];



ArrayList<PVector> kolGrass   = new ArrayList<PVector>();//история количества травы


int max = 0;  //(для графика)

void resetStatistic(){
  for(int i=0; i < kolType; i++) kolBoot[i].clear(); 
  age = 0;
  for(int i=0; i < kolType; i++) kolB[i] = 0; 
}


void adboot(ArrayList<PVector> Int, int kol){
  if(Int.size()>=2){
    if(kol==Int.get(Int.size()-1).y && kol==Int.get(Int.size()-2).y){
      Int.get(Int.size()-1).x++;
      return;
    }
  }
  Int.add(new PVector(age,kol));
  max = max(max, kol);
}


void statistic(){
  //for(int i=0; i < kolType; i++) adboot(kolBoot[i],100*kolB[i]/max(kolB[0],1));
  for(int i=0; i < kolType; i++) adboot(kolBoot[i],kolB[i]);
}


void tablo1(float x, float y, float w, float h)
{
  
  fill(0);
  stroke(50);
  rect(0,0,w,h);
  
  translate(x+100,y+100);
  w-=100;
  h-=200;
  fill(255);
  stroke(255);
  line(0,h,w,h);
  text(age,w-7,h+25);
  line(0,0,0,h);
  
  textAlign(RIGHT);
  //textSize(20);
  textSize(20);
  
  float k = max/h;
  
  for(int i = 0; i <= h; i+=h/10){
    text(int(i*k),-5,h-i);
  }
  
  strokeWeight(1);
  stroke(colorType[0]);
  drawLine(kolBoot[0],w,h,k);
  
  strokeWeight(3);
 
 for(int i = 1; i<kolType; i++){
   stroke(colorType[i]);
   drawLine(kolBoot[i],w,h,k);
 }
 
  strokeWeight(0);

  //translate(-x,-y);
}

void drawLine(ArrayList<PVector> arr ,float w, float h, float k){
  if(arr.size()==0)return;
    float maxX=arr.get(arr.size()-1).x;
  for(int i = 0; i < arr.size()-1; i++){
    line(arr.get(i).x*w/maxX,h-arr.get(i).y/k,arr.get(i+1).x*w/maxX,h-arr.get(i+1).y/k);
  }
}
