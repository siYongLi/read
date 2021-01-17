package net.ninemm.base.word.w2004.elements.tableElements;


import net.ninemm.base.word.api.interfaces.IElement;
import net.ninemm.base.word.api.interfaces.IFluentElementStylable;

import java.util.List;

public class TableRow implements IElement, IFluentElementStylable<TableRowStyle> {

	private StringBuilder txt = new StringBuilder("");
	private TableRowStyle style = new TableRowStyle();
	private final String tableRowTop = "\n <w:tr>{styleRowPh}";
	private final String tableRowBottom = "\n  </w:tr>";
	
	public TableRow(Object[] cols) {
        txt.append(tableRowTop);

        for (int i = 0; i < cols.length; i++) {            
        	//TableCell knows how to do the rest.
        	txt.append( TableCell.with(cols[i]).create().getContent() );
        }
		
        txt.append(tableRowBottom);
	}

	public TableRow(List<Object> cols,Integer emptyNum) {
		txt.append(tableRowTop);

		for (int i = 0; i < cols.size(); i++) {
			//TableCell knows how to do the rest.
			txt.append( TableCell.with(cols.get(i)).create().getContent() );
		}

		if(emptyNum!=null && emptyNum>0){
			for (int j = 0; j < emptyNum; j++) {
				txt.append( TableCell.with("").create().getContent() );
			}
		}

		txt.append(tableRowBottom);
	}

	public TableRow(List<Object> cols) {
		txt.append(tableRowTop);

		for (int i = 0; i < cols.size(); i++) {
			//TableCell knows how to do the rest.
			txt.append( TableCell.with(cols.get(i)).create().getContent() );
		}

		txt.append(tableRowBottom);
	}

	@Override
	public TableRowStyle withStyle() {
		style.setElement(this);
        return style;
	}

	@Override
	public String getContent() {
		String withStyle = style.getNewContentWithStyle(txt.toString());
		return withStyle;
	}

	public static TableRow with(Object ... colls) {		
		return new TableRow(colls);
	}

	public final TableRow create() {
		return this;
	}

}
