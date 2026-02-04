package com.mygdx.game.unit;
import com.mygdx.game.main.Main;
import java.util.ArrayList;

import static com.mygdx.game.main.Main.BlockList2D;
import static com.mygdx.game.method.pow2.pow2;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

public class AI {
    // Координаты ИИ
    private float x, y,xTotal,yTotal;
    public float TargetLine, TargetLineMinBuffer;
    private int TotalIndex = 1;
    private ArrayList<float[]> OpenBlockList = new ArrayList<>();
    private ArrayList<int[]> CloseBlockList = new ArrayList<>();
    private boolean conf,ConfPathAdd;
    public ArrayList<ArrayList<int[]>> IterationPath = new ArrayList<>();

    // Метод для обновления позиции ИИ
    public void pathAIAStar(Unit ai, Unit target, float x_ai, float y_ai){
        ai.path.clear();
        OpenBlockList.clear();
        CloseBlockList.clear();
        int[] target_xy = block_detected_2(target);
        int[] ai_xy = block_detected_3(x_ai, y_ai);
        x = ai_xy[0];
        y = ai_xy[1];
        ai.path.add(new int[]{(int)x, (int)y});
        //IterationPath.add(new ArrayList<>());
        //IterationPath.get(0).add(new int[]{(int)x,(int)y});
        CloseBlockList.add(new int[]{(int)x,(int)y});
        ArrayList<int[]> List;
        while (y != target_xy[1] || x != target_xy[0]) {
            //CloseBlockList.add(new int[]{(int)x,(int)y});
            OpenBlockList.clear();
            xTotal = x + 1;
            if (!BlockList2D.get((int) y).get((int) xTotal).passability & !BlockList2D.get((int) y).get((int) xTotal).AiClose) {
                TargetLine = (float) sqrt(pow2(target_xy[0]-xTotal)+pow2(target_xy[1]-y));
                OpenBlockList.add(new float[]{xTotal, y, TargetLine});
            }
            xTotal = x - 1;
            if (!BlockList2D.get((int) y).get((int) xTotal).passability & !BlockList2D.get((int) y).get((int) xTotal).AiClose) {
                TargetLine = (float) sqrt(pow2(target_xy[0]-xTotal)+pow2(target_xy[1]-y));
                    OpenBlockList.add(new float[]{xTotal, y, TargetLine});
            }
            yTotal = y + 1;
            if (!BlockList2D.get((int) yTotal).get((int) x).passability & !BlockList2D.get((int) yTotal).get((int) x).AiClose) {
                TargetLine = (float) sqrt(pow2(target_xy[0]-x)+pow2(target_xy[1]-yTotal));
                OpenBlockList.add(new float[]{x, yTotal, TargetLine});
            }
            yTotal = y - 1;
            if (!BlockList2D.get((int) yTotal).get((int) x).passability & !BlockList2D.get((int) yTotal).get((int) x).AiClose) {
                TargetLine = (float) sqrt(pow2(target_xy[0]-x)+pow2(target_xy[1]-yTotal));
                OpenBlockList.add(new float[]{x, yTotal, TargetLine});
            }
            if (OpenBlockList.size() == 0) {
                ai.path.remove(ai.path.size()-1);
                x = ai.path.get(ai.path.size()-1)[0];
                y = ai.path.get(ai.path.size()-1)[1];
            } else {
                TargetLineMinBuffer = -1;
                for(int i = 0; i < OpenBlockList.size(); i++){
                    if ((TargetLineMinBuffer == -1)||(OpenBlockList.get(i)[2] < TargetLineMinBuffer)) {
                        TargetLineMinBuffer = OpenBlockList.get(i)[2];
                        x =  OpenBlockList.get(i)[0];
                        y = OpenBlockList.get(i)[1];
                    }
                }
                ai.path.add(new int[]{(int)x, (int)y});
                BlockList2D.get((int) y).get((int) x).AiClose = true;
                CloseBlockList.add(new int[]{(int)x, (int)y});
                OpenBlockList.clear();
            }

        }
        for(int[] block:CloseBlockList){
            BlockList2D.get(block[1]).get(block[0]).AiClose = false;
        }
        CloseBlockList.clear();
        OpenBlockList.clear();
    }
    public void pathAI(Unit ai, Unit target, float x_ai, float y_ai){
        ai.path.clear();
        IterationPath.clear();
        OpenBlockList.clear();
        CloseBlockList.clear();
        int[] target_xy = block_detected_2(target);
        int[] ai_xy = block_detected_3(x_ai, y_ai);
        x = ai_xy[0];
        y = ai_xy[1];
        ai.path.add(new int[]{(int)x, (int)y});
        IterationPath.add(new ArrayList<>());
        IterationPath.get(0).add(new int[]{(int)x,(int)y});
        CloseBlockList.add(new int[]{(int)x,(int)y});
        ArrayList<int[]> List;
        while (y != target_xy[1] || x != target_xy[0]) {
            for(int i2 = 0;i2<IterationPath.size();i2++) {
                ArrayList<int[]>pathAi = IterationPath.get(i2);
                x = pathAi.get(pathAi.size()-1)[0];
                y = pathAi.get(pathAi.size()-1)[1];
                //CloseBlockList.add(new int[]{(int)x,(int)y});
                OpenBlockList.clear();
                xTotal = x + 1;
                if (!BlockList2D.get((int) y).get((int) xTotal).passability & !BlockList2D.get((int) y).get((int) xTotal).AiClose) {
                    OpenBlockList.add(new float[]{xTotal, y, TargetLine});
                }
                xTotal = x - 1;
                if (!BlockList2D.get((int) y).get((int) xTotal).passability & !BlockList2D.get((int) y).get((int) xTotal).AiClose) {
                    OpenBlockList.add(new float[]{xTotal, y, TargetLine});
                }
                yTotal = y + 1;
                if (!BlockList2D.get((int) yTotal).get((int) x).passability & !BlockList2D.get((int) yTotal).get((int) x).AiClose) {
                    OpenBlockList.add(new float[]{x, yTotal, TargetLine});
                }
                yTotal = y - 1;
                if (!BlockList2D.get((int) yTotal).get((int) x).passability & !BlockList2D.get((int) yTotal).get((int) x).AiClose) {
                    OpenBlockList.add(new float[]{x, yTotal, TargetLine});
                }
                if (OpenBlockList.size() == 0) {
                    IterationPath.remove(pathAi);
                    break;
                } else {
                    for (float[] ints : OpenBlockList) {
                        if (target_xy[0] == ints[0] & target_xy[1] == ints[1]) {
                            ai.path = (ArrayList<int[]>) pathAi.clone();
                            ai.path.add(new int[]{(int) ints[0], (int) ints[1]});
                            for (int[] intss : CloseBlockList) {
                                BlockList2D.get(intss[1]).get(intss[0]).AiClose = false;
                            }
                            return;
                        }
                        List = (ArrayList<int[]>) pathAi.clone();
                        List.add(new int[]{(int) ints[0], (int) ints[1]});
                        CloseBlockList.add(new int[]{(int) ints[0], (int) ints[1]});
                        BlockList2D.get((int) ints[1]).get((int) ints[0]).AiClose = true;
                        IterationPath.add(List);
                        IterationPath.remove(pathAi);

                    }
                }
            }
        }
    }
    public static float TargetGet(float x,float y,float x2,float y2){
        return  abs(x2-x)+abs(y2-y);
    }
    public static float TargetGet2(float x,float y,float x2,float y2){
        return  (float) sqrt(pow2(x2-x)+pow2(y2-y));
    }
    public int[] block_detected_2(Unit tr){
        //System.out.println(tr.ID);
        int i = (int)(tr.tower_y/Main.width_block)-1;
        int i2 = (int)(tr.tower_x/Main.width_block)-1;
        //Main.BlockList2D.get(i).get(i2).render_block = UpdateRegister.Update3;
        //System.out.println(tr.ID);
        if(!BlockList2D.get(i).get(i2).passability) {
            return new int[]{i2, i};
        }
        else{
            if(!BlockList2D.get(i+1).get(i2).passability){
                return new int[]{i2, i-1};
            }
            else if(!BlockList2D.get(i-1).get(i2).passability){
                return new int[]{i2, i+1};
            }
            else if(!BlockList2D.get(i).get(i2+1).passability){
                return new int[]{i2-1, i};
            }
            else if(!BlockList2D.get(i).get(i2-1).passability){
                return new int[]{i2+1, i};
            }
        }
        return new int[]{i2, i};
    }
    public int[] block_detected_3(float x,float y){
        int i = (int)(y/Main.width_block)-1;
        int i2 = (int)(x/Main.width_block)-1;
        if(!BlockList2D.get(i).get(i2).passability) {
            return new int[]{i2, i};
        }
        else{
            if(!BlockList2D.get(i+1).get(i2).passability){
                return new int[]{i2, i+1};
            }
            else if(!BlockList2D.get(i-1).get(i2).passability){
                return new int[]{i2, i-1};
            }
            else if(!BlockList2D.get(i).get(i2+1).passability){
                return new int[]{i2+1, i};
            }
            else if(!BlockList2D.get(i).get(i2-1).passability){
                return new int[]{i2-1, i};
            }
        }
        return new int[]{i2, i};
    }
}
