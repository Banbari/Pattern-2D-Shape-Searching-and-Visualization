import java.io.*;
import java.util.*;

 class KLA_TANCOR {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)throws Exception {
        FileWriter fw=new FileWriter(new File("temp1.txt"));
        FileReader fr=new FileReader("3_8_Million_POI.txt");
        BufferedReader br=new BufferedReader(fr);

        ArrayList<ArrayList<Integer>> ar=new ArrayList<>();
        int layer=-1;
        String s=null;
        int count=0;
        Measure m=null;
        for(int i=0;i<6;i++)
            br.readLine();
        while((s=br.readLine())!=null)
        {
            if(s.equals("endstr") || s.equals("endlib"))
                break;
            StringTokenizer layerData=new StringTokenizer(br.readLine().trim()," ");
            s=layerData.nextToken();
            if(s.equals("endstr") || s.equals("endlib"))
                break;
            layer=Integer.parseInt(layerData.nextToken());
            br.readLine();                                              //datatype line
            String[] layerData1=br.readLine().trim().split("\\s+");                          
            int n=Integer.parseInt(layerData1[1])/4;
           for(int i=2;i<layerData1.length;i+=2)
               {
                ArrayList<Integer> temp=new ArrayList<>(2);
                temp.add(Integer.parseInt(layerData1[i]));
                temp.add(Integer.parseInt(layerData1[i+1]));
                ar.add(temp);
                
               }
            for(int j=0;j<n;j++)
            {
               String data[]=(br.readLine().trim().split("\\s+"));     //next Line
               for(int i=0;i<data.length;i+=2)
               {
                ArrayList<Integer> temp=new ArrayList<>(2);
                temp.add(Integer.parseInt(data[i]));
                temp.add(Integer.parseInt(data[i+1]));
                ar.add(temp);
               }

            }
            m=new Measure(ar);
            m.calDistanceAndAngle();
            s=br.readLine();
            if(s.equals("endstr"))
                break;
        }
        fw.close();
        fr.close();
        /*System.out.println("Hello "+ar.size());
        for(int i=0;i<ar.size();i++)
        {
                System.out.println(ar.get(i).get(0)+" "+ar.get(i).get(1));
        }*/
        fr=new FileReader("3_8_Million_source.txt");
        fw=new FileWriter("output.txt");
        br=new BufferedReader(fr);
        ArrayList<ArrayList<Integer>> fileData=null;
        for(int i=0;i<6;i++)
            br.readLine();
        while((s=br.readLine())!=null)
        {
            if(s.equals("endstr"))
                break;
            fileData=new ArrayList<>();
            StringTokenizer layerData=new StringTokenizer(br.readLine().trim()," ");
            if(layerData.nextToken().equals("endlib"))
                break;
            layer=Integer.parseInt(layerData.nextToken());
            br.readLine();                                              //datatype line
            String[] layerData1=br.readLine().trim().split("\\s+");
                                 
            int n=Integer.parseInt(layerData1[1])/4;
           for(int i=2;i<layerData1.length;i+=2)
               {
                ArrayList<Integer> temp=new ArrayList<>(2);
                temp.add(Integer.parseInt(layerData1[i]));
                temp.add(Integer.parseInt(layerData1[i+1]));
                fileData.add(temp);
               }
            for(int j=0;j<n;j++)
            {
               String data[]=(br.readLine().trim().split("\\s+"));     //next Line
               for(int i=0;i<data.length;i+=2)
               {
                ArrayList<Integer> temp=new ArrayList<>(2);
                temp.add(Integer.parseInt(data[i]));
                temp.add(Integer.parseInt(data[i+1]));
                fileData.add(temp);
               }
            }
            Measure measure=new Measure(fileData);
            if(fileData.size()==ar.size())
            measure.calDistanceAndAngle();
            if(fileData.size()==ar.size() && measure.equals(m))
            {
                fw.write("boundary "+System.lineSeparator());
                fw.write("layer "+layer+System.lineSeparator());
                fw.write("datatype 0"+System.lineSeparator());
                fw.write("xy   "+fileData.size()+"   ");
                for(int i=0;i<fileData.size();i++)
                {
                 fw.write(fileData.get(i).get(0)+" "+fileData.get(i).get(1));
                 if(i!=fileData.size()-1)
                    fw.write("   ");
             }
             fw.write(System.lineSeparator()+"endel"+System.lineSeparator());
             count++;
            }
            s=br.readLine();
            if(s.equals("endstr"))
                break;
        }
        System.out.println(count);
        fw.close();
        fr.close();
    } 
    static class Measure
    {
        ArrayList<Double> length=null;
        ArrayList<Double> angle=null;
        ArrayList<ArrayList<Integer>> data=null;
        Measure(ArrayList<ArrayList<Integer>> ar)
        {
            data=ar;
        }
        public  void calDistanceAndAngle()
        {
            int n=data.size();
            length=new ArrayList<>();
            angle=new ArrayList<>();
            double m1=0.0,m2=0.0,m3=0.0;
            int x1=0,y1=0,x2=0,y2=0,x3=0,y3=0;
            for(int i=1;i<n;i++)
            {
                x1=data.get(i).get(0).intValue();
                y1=data.get(i).get(1).intValue();
                x2=data.get(i-1).get(0).intValue();
                y2=data.get(i-1).get(1).intValue();
                length.add(Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2)));
                int k=i-1;
                if(k==0)
                    k=n;
                x3=data.get(k-1).get(0).intValue();
                y3=data.get(k-1).get(1).intValue();
                m1=findAngle(x1,y1,x2,y2,x3,y3);
                angle.add(m1);
            }
                x1=data.get(0).get(0).intValue();
                y1=data.get(0).get(1).intValue();
                x2=data.get(n-1).get(0).intValue();
                y2=data.get(n-1).get(1).intValue();
                x3=data.get(n-2).get(0).intValue();
                y3=data.get(n-2).get(1).intValue();
                length.add(Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2)));
                m1=findAngle(x1,y1,x2,y2,x3,y3);
                angle.add(m1);
            }
            public double findAngle(int x1,int y1,int x2,int y2,int x3,int y3)
            {
                double x=((x3-x2)*(x2-x1))-((y3-y2)*(y2-y1));
                if(x==0)
                    return 90.0;
                double y=((y3-y2)*(x2-x1))-((y2-y1)*(x2-x1));
                if(y==0.0)
                    return 0.0;
                return y/x;
            }
            public boolean equals(Measure m)
            {
                int k=0;
                int n=length.size();
                for(int i=0;i<n;i++)
                {
                    if(length.get(i).compareTo(m.length.get(0))==0 && angle.get(i).compareTo(m.angle.get(0))==0)
                    {
                        k=i;
                        break;
                    }
                }
                for(int i=0;i<n;i++)
                {
                    if(length.get((i+k)%n).compareTo(m.length.get(i))!=0)
                        return false;
                }
                for(int i=0;i<length.size();i++)
                {
                    if(length.get((i+k)%n).compareTo(m.length.get(i))!=0)
                        return false;
                }
                for(int i=0;i<angle.size();i++)
                {
                    if(angle.get((i+k)%n).compareTo(m.angle.get(i))!=0)
                        return false;
                }
                return true;
            }
    }   
}
