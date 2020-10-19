package muuttaa.myohemmin.realistisenelamansimulaattori.scenariocreation;

public interface ModifiableInterface {
    /**
     * Duplicates the modifiable item at the position.
     * @param position position where the item is in array
     */
    void duplicateModifiable(int position);

    /**
     * Moves the modifiable item at the position.
     * @param position position where the item is in array
     */
    void moveModifiable(int position, boolean up);

    /**
     * Deletes the modifiable item at the position.
     * @param position position where the item is in array
     */
    void deleteModifiable(int position);
}
