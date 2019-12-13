import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './craftsperson.reducer';
import { ICraftsperson } from 'app/shared/model/craftsperson.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICraftspersonDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class CraftspersonDetail extends React.Component<ICraftspersonDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { craftspersonEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Craftsperson [<b>{craftspersonEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="firstName">First Name</span>
            </dt>
            <dd>{craftspersonEntity.firstName}</dd>
            <dt>
              <span id="lastName">Last Name</span>
            </dt>
            <dd>{craftspersonEntity.lastName}</dd>
            <dt>
              <span id="email">Email</span>
            </dt>
            <dd>{craftspersonEntity.email}</dd>
            <dt>Mentor</dt>
            <dd>{craftspersonEntity.mentor ? craftspersonEntity.mentor.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/craftsperson" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/craftsperson/${craftspersonEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ craftsperson }: IRootState) => ({
  craftspersonEntity: craftsperson.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CraftspersonDetail);
