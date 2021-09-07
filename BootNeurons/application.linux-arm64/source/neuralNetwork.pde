class NeuralNetwork
{
  ArrayList<ArrayList<Neuron>> network = new ArrayList<ArrayList<Neuron>>();
  ArrayList<Neuron> layer = new ArrayList<Neuron>();
  
  int id=0;
  
  NeuralNetwork(){}
  
  void addNeuron(float deff)
  {
    Neuron n = new Neuron(deff);
    layer.add(n);
    
    n.id=id;
    id++;
  }
  void addNeuron(float deff, float ... weight)
  {
    Neuron n = new Neuron(deff,network.get(0),weight);
    layer.add(n);
    
    n.id=id;
    id++;
  }
  
  void addNeuronR(float deff)
  {
    Neuron n = new Neuron(random(-deff,+deff));
    layer.add(n);
    
    n.id=id;
    id++;
  }
  void addNeuronR(float deff, float weight)
  {
    float[] w = new float[network.get(0).size()];
    for(int i=0; i<network.get(0).size(); i++)
    w[i]=random(-weight,weight);
    
    Neuron n = new Neuron(random(-deff,+deff),network.get(0),w);
    layer.add(n);
    
    
    n.id=id;
    id++;
  }
  
  void setLayer()
  {
    ArrayList<Neuron> l = new ArrayList<Neuron>();
    l.addAll(layer);
    network.add(0,l);
    layer.clear();
  }
  void addLayerR(int kolLayer, int kolNeuron, float deff, float weight)
  {
    for(int i=0;i<kolLayer;i++)
    {
      for(int j=0;j<kolNeuron;j++)
      {
        addNeuronR(deff,weight);
      }
      setLayer();
    }
  }
  void addLayerR(int kolNeuron, float deff)
  {
    for(int j=0;j<kolNeuron;j++)
    {
      addNeuronR(deff);
    }
    setLayer();
  }
  
  ///////////////
  
  void display()
  {
    
    for(Neuron n : network.get(0))
    n.disp();
    
    for(int i=1; i<network.size(); i++)
    for(Neuron n : network.get(i))
    n.display();
    
  }
  
  ///////////////
  
  void in(ArrayList<Float> i)
  {
    if(i.size()==network.get(0).size())
      for(int j=0; j<network.get(0).size(); j++)
        network.get(0).get(j).in=i.get(j)+network.get(0).get(j).deff;
    
    else if(i.size()>network.get(0).size())
    {
      println("Error: count of parametrs more");
      println("than count of input neurons.");
    }
    else if(i.size()<network.get(0).size())
    {
      println("Error: count of parametrs less");
      println("than count of input neurons.");
    }
  }
  
  void in(float ... i)
  {
    if(i.length==network.get(0).size())
      for(int j=0; j<network.get(0).size(); j++)
        network.get(0).get(j).in=i[j]+network.get(0).get(j).deff;
    
    else if(i.length>network.get(0).size())
    {
      println("Error: count of parametrs more");
      println("than count of input neurons.");
      exit();
    }
    else if(i.length<network.get(0).size())
    {
      println("Error: count of parametrs less");
      println("than count of input neurons.");
      exit();
    }
  }
  
  float out(int i)
  {
    return network.get(network.size()-1).get(i).out;
  }
  
  //////////////
  
  
  NeuralNetwork clone()
  {
    NeuralNetwork net = new NeuralNetwork();
    
    for(Neuron n : network.get(network.size()-1))
      {
        net.addNeuron(n.deff);
      }
    net.setLayer();
    for(int i = network.size()-2; i>=0; i--)
    {
      for(Neuron n : network.get(i))
      {
        net.addNeuron(n.deff,n.weight.clone());
      }
      net.setLayer();
    }
    
    return net;
  }
  
  
  void changeWeight(float r)
  {
    for(int i=0; i<network.size(); i++)
    for(Neuron n : network.get(i)){
      n.changeWeight(r);
    }
  }
  
}
