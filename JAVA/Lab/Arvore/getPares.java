public class getPares{
    public getPares(No i){
        int out= 0;
        if(i!=null){
            out= getPares(i.elemento%2==0) + getPares(i.esq)+ getPares(i.dir);
        }
        return out;
    }


}