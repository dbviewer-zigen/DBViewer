package zigen.plugin.db.core.rule;

import java.math.BigDecimal;

import zigen.plugin.db.core.rule.SequenceInfo;

public class SymfowareSequenceInfo extends SequenceInfo {

	private static final long serialVersionUID = 1L;


	private BigDecimal cache_min_size;


	public BigDecimal getCache_min_size() {
		return cache_min_size;
	}


	public void setCache_min_size(BigDecimal cacheMinSize) {
		cache_min_size = cacheMinSize;
	}

}
