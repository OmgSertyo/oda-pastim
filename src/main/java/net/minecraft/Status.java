package net.minecraft;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Status {

    @Getter
    public final String name;
        public final CompactAnimation animationY = new CompactAnimation(Easing.EASE_IN_OUT_QUAD, 50);
        public final CompactAnimation animationAlpha = new CompactAnimation(Easing.EASE_OUT_CUBIC, 220);

        public void update() {
            animationY.run(12);
        }
        public void setAlpha(float alpha) {
            animationAlpha.run(alpha);
        }
    }