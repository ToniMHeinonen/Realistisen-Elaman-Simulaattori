package muuttaa.myohemmin.realistisenelamansimulaattori.tools;

public class MoveCheck {

    private boolean canMoveUp = false;
    private boolean canMoveDown = false;

    public MoveCheck(int listSize, int index) {
        // If there is only 1 item in list, movement is not possible
        if (listSize == 1) {
            return;
        }

        if (index == 0) {
            // If item is at the beginning of list, down is possible
            canMoveDown = true;
        } else if (index == listSize - 1) {
            // If item is at the end of list, up is possible
            canMoveUp = true;
        } else {
            // Else all the movement is possible
            canMoveDown = true;
            canMoveUp = true;
        }
    }

    public boolean isCanMoveUp() {
        return canMoveUp;
    }

    public boolean isCanMoveDown() {
        return canMoveDown;
    }
}
