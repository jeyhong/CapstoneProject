package components;

import imgui.ImGui;
import imgui.type.ImBoolean;
import imgui.type.ImString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class StateMachine extends Component{
    private class StateTrigger {
        public String state;
        public String trigger;

        public StateTrigger(){}
        public StateTrigger(String state, String trigger){
            this.state = state;
            this.trigger = trigger;
        }

        @Override
        public boolean equals(Object o){
            if(o.getClass() != StateTrigger.class) return false;
            StateTrigger sT = (StateTrigger) o;
            return sT.trigger.equals(this.trigger) && sT.equals(this.state);
        }

        @Override
        public int hashCode(){
            return Objects.hash(trigger, state);
        }
    }

    public HashMap<StateTrigger, String> stateTransfers = new HashMap<>();
    private List<AnimationState> states = new ArrayList<>();
    private transient AnimationState currState = null;
    private String defStateTitle = "";

    public void refreshTex(){
        for(AnimationState state : states){
            state.refreshTex();
        }
    }
    public void addStateTrigger(String from, String to, String onTrigger){
        this.stateTransfers.put(new StateTrigger(from, onTrigger ), to);
    }

    public void addState(AnimationState state){
        this.states.add(state);
    }

    public void setDefState(String animationTitle){
        for(AnimationState state : states){
            if(state.title.equals(animationTitle)){
                defStateTitle = animationTitle;
                if(currState == null){
                    currState = state;
                    return;
                }
            }
        }
        System.out.println("Unable to find state '" + animationTitle + "' in set default state");
    }
    public void trigger(String trigger){
        for(StateTrigger state: stateTransfers.keySet()){
            if(state.state.equals(currState.title) && state.trigger.equals(trigger)){
                if(stateTransfers.get(state) != null){
                    int newStateIndex = -1;
                    int index = 0;
                    for(AnimationState aS : states){
                        if(aS.title.equals(stateTransfers.get(state))){
                            newStateIndex = index;
                            break;
                        }
                        index++;
                    }

                    if(newStateIndex > -1){
                        currState = states.get(newStateIndex);
                    }
                }
                return;
            }
        }
        System.out.println("Unable to find trigger: " + trigger);
    }

    @Override
    public void start(){
        for(AnimationState state: states){
            if(state.title.equals(defStateTitle)){
                currState = state;
                break;
            }
        }
    }

    @Override
    public void update(float dt){
        if(currState != null){
            currState.update(dt);
            SpriteRenderer spr = gameObj.getComponent(SpriteRenderer.class);
            if(spr != null){
                spr.setSprite(currState.getCurrSpr());
            }
        }
    }

    @Override
    public void editorUpdate(float dt){
        if(currState != null){
            currState.update(dt);
            SpriteRenderer spr = gameObj.getComponent(SpriteRenderer.class);
            if(spr != null){
                spr.setSprite(currState.getCurrSpr());
            }
        }
    }

    @Override
    public void imgui(){
        int index = 0;
        for(AnimationState state: states){
            ImString title = new ImString(state.title);
            ImGui.inputText("State: ", title);
            state.title = title.get();

            ImBoolean doesLoop = new ImBoolean(state.doesLoop);
            ImGui.checkbox("Does Loop? ", doesLoop);
            state.setLoop(doesLoop.get());
            for(Frame frame : state.animationFrames){
                float[] temp = new float[1];
                temp[0] = frame.frameTime;
                ImGui.dragFloat("Frame(" + index + ") Time : ", temp, 0.01f);
                frame.frameTime = temp[0];
            }
        }
    }
}
