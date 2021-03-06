// CloudCoder - a web-based pedagogical programming environment
// Copyright (C) 2011-2013, Jaime Spacco <jspacco@knox.edu>
// Copyright (C) 2011-2013, David H. Hovemeyer <david.hovemeyer@gmail.com>
// Copyright (C) 2013, York College of Pennsylvania
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU Affero General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Affero General Public License for more details.
//
// You should have received a copy of the GNU Affero General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.

package org.cloudcoder.app.server.persist.txn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.cloudcoder.app.server.persist.util.AbstractDatabaseRunnableNoAuthException;
import org.cloudcoder.app.server.persist.util.DBUtil;
import org.cloudcoder.app.shared.model.RepoProblemRating;

/**
 * Transaction to get a list of {@link RepoProblemRating}s for a given
 * repository problem (exercise).
 * 
 * @author David Hovemeyer
 */
public class GetRatingsForRepoProblem extends
		AbstractDatabaseRunnableNoAuthException<List<RepoProblemRating>> {

	private int repoProblemId;

	public GetRatingsForRepoProblem(int repoProblemId) {
		this.repoProblemId = repoProblemId;
	}

	@Override
	public String getDescription() {
		return "get ratings for repo problem";
	}

	@Override
	public List<RepoProblemRating> run(Connection conn) throws SQLException {
		PreparedStatement stmt = prepareStatement(
				conn,
				"select r.* from cc_repo_problem_ratings as r " +
				" where r.repo_problem_id = ?"
		);
		stmt.setInt(1, repoProblemId);
		
		List<RepoProblemRating> result = new ArrayList<RepoProblemRating>();
		
		ResultSet resultSet = executeQuery(stmt);
		while (resultSet.next()) {
			RepoProblemRating rating = new RepoProblemRating();
			DBUtil.loadModelObjectFields(rating, RepoProblemRating.SCHEMA, resultSet);
			result.add(rating);
		}
		
		return result;
	}

}
