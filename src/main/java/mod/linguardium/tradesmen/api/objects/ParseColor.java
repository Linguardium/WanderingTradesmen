package mod.linguardium.tradesmen.api.objects;

import net.minecraft.client.util.math.Vector3f;

import java.io.InvalidObjectException;
import java.util.ArrayList;

public class ParseColor {
    public static int toInt(Object oColor) throws InvalidObjectException {
        oColor = ObjectConversion.toJavaObject(oColor);
        if (oColor instanceof float[]) {
            if (((float[]) oColor).length!=3)
                throw new InvalidObjectException("Array length expected 3. Array given was length: "+String.valueOf(((float[]) oColor).length));
            float[] color = (float[])oColor;
            if (color.length!=3)
                return 0xFF00FF;
            float r = Math.max(Math.min(color[0],1.0F),0.0F);
            float g = Math.max(Math.min(color[1],1.0F),0.0F);
            float b = Math.max(Math.min(color[2],1.0F),0.0F);
            if (r!=color[0] || g!=color[1] || b!=color[2])
                return 0xFF00FF;
            r*=0xFF0000;
            g*=0xFF00;
            b*=0xFF;
            int intColor=0;
            intColor = (int)r&0xFF0000;
            intColor |= (int)g&0xFF00;
            intColor |= (int)b&0xFF;
            return intColor;
        }else if (oColor instanceof Integer) {
            return ((Integer) oColor).intValue();
        }else if (oColor instanceof Long) {
            return ((Long) oColor).intValue();
        }else if (oColor instanceof Vector3f) {
            return toInt(new float[]{((Vector3f) oColor).getX(),((Vector3f) oColor).getY(),((Vector3f) oColor).getZ()});
        }else if (oColor instanceof double[]) {
            double[] color=(double[])oColor;
            return toInt(new float[]{(float)color[0],(float)color[1],(float)color[2]});
        }else if (oColor instanceof ArrayList) {
            if (((ArrayList) oColor).size()!=3) {
                throw new InvalidObjectException("Array length expected 3. Array given was length: "+String.valueOf(((ArrayList) oColor).size()));
            }
            Object r = ((ArrayList) oColor).get(0);
            Object g = ((ArrayList) oColor).get(1);
            Object b = ((ArrayList) oColor).get(2);
            if (r instanceof Integer) {
                return (((Integer)r&0xFF)<<16) | (((Integer)g&0xFF)<<8) | ((Integer)b&0xFF);
            }else if(r instanceof Double) {
                return toInt(new float[]{((Double) r).floatValue(),((Double)g).floatValue(),((Double)b).floatValue()});
            }else if(r instanceof Float) {
                return toInt(new float[]{(float)r,(float)g,(float)b});
            }
        }

        return 0;
    }
    public static float[] toFloat(Object oColor) throws InvalidObjectException {
        oColor = ObjectConversion.toJavaObject(oColor);

        if (oColor instanceof float[]) {
            if (((float[]) oColor).length!=3)
                throw new InvalidObjectException("Array length expected 3. Array given was length: "+String.valueOf(((float[]) oColor).length));
            return (float[])oColor;
        }else if (oColor instanceof Vector3f) {
            return new float[]{((Vector3f) oColor).getX(),((Vector3f) oColor).getY(),((Vector3f) oColor).getZ()};
        }else if (oColor instanceof double[]) {
            if (((double[]) oColor).length!=3)
                throw new InvalidObjectException("Array length expected 3. Array given was length: "+String.valueOf(((double[]) oColor).length));
            double[] color=(double[])oColor;
            return new float[]{(float)color[0],(float)color[1],(float)color[2]};
        }else if (oColor instanceof Integer) {
            int color = (int)oColor;
            float r = ((color>>16)&0xFF)/255.0F;
            float g = ((color>>8)&0xFF)/255.0F;
            float b = (color&0xFF)/255.0F;
            return new float[]{r,g,b};
        }else if (oColor instanceof ArrayList) {
            if (((ArrayList) oColor).size()!=3) {
                throw new InvalidObjectException("Array length expected 3. Array given was length: "+String.valueOf(((ArrayList) oColor).size()));
            }
            Object r = ((ArrayList) oColor).get(0);
            Object g = ((ArrayList) oColor).get(1);
            Object b = ((ArrayList) oColor).get(2);
            if (r instanceof Integer) {
                return new float[]{(((Integer)r&0xFF)/255.0F),(((Integer)g&0xFF)/255.0F) , ((Integer)b&0xFF)/255.0F};
            }else if(r instanceof Double) {
                return new float[]{((Double) r).floatValue(),((Double)g).floatValue(),((Double)b).floatValue()};
            }else if(r instanceof Float) {
                return new float[]{(float)r,(float)g,(float)b};
            }

        }
        return new float[]{0F,0F,0F};
    }
}
