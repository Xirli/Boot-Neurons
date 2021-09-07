class Neuron
{
  float deff; //start value
  float in;
  float out;
  ArrayList<Neuron> links = new ArrayList<Neuron>();
  float[] weight;
  
  int id;
  
  Neuron(float d)
  {
    deff = d;
    in   = d;
  }
  Neuron(float d, ArrayList<Neuron> l, float[] w)
  {
    deff   = d;
    in     = d;
    links  = l;
    weight = w;
  }
  
  
  
  float f2()
  {
         if(in>1)return  1.1;
    else if(in<1)return  0.9;
    else         return  1;
  }
  float f1()
  {
         if(in>0)return  1;
    else if(in<0)return -1;
    else         return  0;
  }
  float f()
  {
    return 2*atan(in)/PI;
  }
  float f3()
  {
    return cos(f()*PI);
  }
  float ReL(){
    return min(0,in);
  }
  
  /////////////
  
  void display()
  {
    out = in;
    
    for(int i=0; i<links.size(); i++)
    links.get(i).in += out * weight[i];
    
    in = deff;
  }
  void disp()
  {
    out = in;
    
    for(int i=0; i<links.size(); i++)
    {
      links.get(i).in += out * weight[i];
    }
    
    in = deff;
  }
  
  /////////////
  
  void changeWeight(float r)
  {
    
    if(r>0.5)
    {
      deff+=random(-r,r);
      for(int i=0; i<links.size(); i++)
    {
      weight[i]+=random(-r,r);
    }
    }
    
    //deff=100;
    deff+=deff*random(-r,r);
    //if(abs(deff)<0.1)deff+=random(-1,1);
    //if(abs(deff)>500)deff*=0.99;
    for(int i=0; i<links.size(); i++)
    {
      weight[i]+=weight[i]*random(-r,r);
      //weight[i]=random(-0.01,0.01);
      //if(abs(weight[i])<0.01)weight[i]+=random(-0.1,0.1);
      //if(abs(weight[i])>32)weight[i]/=2;
    }
  }
  
}
