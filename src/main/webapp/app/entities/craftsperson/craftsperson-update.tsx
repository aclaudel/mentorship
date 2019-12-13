import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntities as getCraftspeople } from 'app/entities/craftsperson/craftsperson.reducer';
import { getEntity, updateEntity, createEntity, reset } from './craftsperson.reducer';
import { ICraftsperson } from 'app/shared/model/craftsperson.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICraftspersonUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ICraftspersonUpdateState {
  isNew: boolean;
  menteesId: string;
  mentorId: string;
}

export class CraftspersonUpdate extends React.Component<ICraftspersonUpdateProps, ICraftspersonUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      menteesId: '0',
      mentorId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (!this.state.isNew) {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getCraftspeople();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { craftspersonEntity } = this.props;
      const entity = {
        ...craftspersonEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/craftsperson');
  };

  render() {
    const { craftspersonEntity, craftspeople, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="mentorshipApp.craftsperson.home.createOrEditLabel">Create or edit a Craftsperson</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : craftspersonEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="craftsperson-id">ID</Label>
                    <AvInput id="craftsperson-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="firstNameLabel" for="craftsperson-firstName">
                    First Name
                  </Label>
                  <AvField id="craftsperson-firstName" type="text" name="firstName" />
                </AvGroup>
                <AvGroup>
                  <Label id="lastNameLabel" for="craftsperson-lastName">
                    Last Name
                  </Label>
                  <AvField id="craftsperson-lastName" type="text" name="lastName" />
                </AvGroup>
                <AvGroup>
                  <Label id="emailLabel" for="craftsperson-email">
                    Email
                  </Label>
                  <AvField id="craftsperson-email" type="text" name="email" />
                </AvGroup>
                <AvGroup>
                  <Label for="craftsperson-mentor">Mentor</Label>
                  <AvInput id="craftsperson-mentor" type="select" className="form-control" name="mentor.id">
                    <option value="" key="0" />
                    {craftspeople
                      ? craftspeople.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/craftsperson" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp; Save
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  craftspeople: storeState.craftsperson.entities,
  craftspersonEntity: storeState.craftsperson.entity,
  loading: storeState.craftsperson.loading,
  updating: storeState.craftsperson.updating,
  updateSuccess: storeState.craftsperson.updateSuccess
});

const mapDispatchToProps = {
  getCraftspeople,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CraftspersonUpdate);
